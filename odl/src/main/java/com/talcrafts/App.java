package com.talcrafts;

import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.TypeDefinition;
import org.opendaylight.yangtools.yang.model.parser.api.YangSyntaxErrorException;
import org.opendaylight.yangtools.yang.model.repo.api.SchemaSourceException;
import org.opendaylight.yangtools.yang.parser.repo.YangTextSchemaContextResolver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting Yang Parser!");
        printAllTypes("/home/ashishwaghmare/Hacks/tonos/odl/src/main/resources");
        //printAllTypes("/home/ashishwaghmare/Hacks/openconfig-public/release/models");
        //printAllTypes("/home/ashishwaghmare/Hacks/yang/standard");
    }

    public static void printAllTypes(String folder) throws IOException {
        final YangTextSchemaContextResolver yangContextResolver = YangTextSchemaContextResolver.create("yang-context-resolver");

        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths.filter(Files::isRegularFile).map(path -> path.toString()).filter(file -> file.endsWith(".yang")).forEach(file -> {
                try {
                    yangContextResolver.registerSource(new URL("file://" + file));
                } catch (SchemaSourceException | IOException | YangSyntaxErrorException e) {
                    e.printStackTrace();
                }
            });
        }

        //yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/my.yang"));
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-inet-types@2013-07-15.yang"));
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-yang-types@2013-07-15.yang"));
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-yang-library@2016-06-21.yang"));
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-ipfix-psamp@2012-09-05.yang"));
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-netconf-acm@2012-02-22.yang"));
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-netconf-monitoring@2010-10-04.yang"));
//
//
//        yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-snmp@2014-12-10.yang"));

        //Problem below
        //yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-netconf-notifications@2012-02-06.yang"));
        // Problem other typedef don't load
        //yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/iana-crypt-hash@2014-08-06.yang"));
        // Depends on above
        //yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-system@2014-08-06.yang"));

        //Problem context
        //yangContextResolver.registerSource(new URL("file:///home/ashishwaghmare/Hacks/tonos/odl/src/main/resources/ietf-complex-types@2011-03-15.yang"));
        Optional<? extends SchemaContext> sc = yangContextResolver.getSchemaContext();
        if (sc.isPresent()) {
            SchemaContext schemaContext = sc.get();
            Collection<? extends Module> modules = schemaContext.getModules();
            for (Module module : modules) {
                module.getTypeDefinitions().forEach(App::print);
                module.getChildNodes().stream().forEach(i -> System.out.println(i));
            }
        }
    }


    public static void print(TypeDefinition typeDefinition) {
        QName qName = typeDefinition.getQName();
        System.out.println(qName);
    }
}
