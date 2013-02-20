vining
=======

virtualenv venv --distribute  

pip install -r requirement.txt  

mvn -f pom.xml compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=me.haogao.vining.topology.ViningTopology
