import pyangbind.lib.pybindJSON as pybindJSON
import machine

my = machine.my
my.system.hostname.add("test")


print(pybindJSON.dumps(my))