vining
=======
1.install maven first  

2. pip install virtualenv  

3. virtualenv venv --distribute  
source venv/bin/activate  

4. pip install -r requirements.txt  

mvn -f pom.xml compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=me.haogao.vining.topology.ViningTopology

mvn -f pom.xml compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=me.haogao.vining.topology.ViningTweepyTopology  
