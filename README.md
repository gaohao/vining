vining
=======
1. Install maven3 first  
  On OS X:
  <pre>
  brew install maven
  </pre>

2. Install virtualenv  
<pre>pip install virtualenv</pre>  

3. Install and initial an virtualenv  
<pre>
cd vinig-repo  
virtualenv venv --distribute  
source venv/bin/activate
</pre>

4. Install all python dependency  
<pre>pip install -r requirements.txt</pre>

5. <a href="http://redis.io/download">Install Redis</a> and execute ./redis-server  

6. Run the topology  
<pre>
mvn -f pom.xml compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=me.haogao.vining.topology.ViningShellTopology    
mvn -f pom.xml compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=me.haogao.vining.topology.ViningTweepyTopology  
</pre>

How configure on Ubuntu:  

Install maven 3  
http://yarovoy.com/post/14363197336/maven-how-to-install-maven-3-on-ubuntu-11-10  

Install pip  
http://www.saltycrane.com/blog/2010/02/how-install-pip-ubuntu/  

Problems:  
1. locale.Error: unsupported locale setting  
  Sudo locale-gen en_US.UTF-8  
2. For long run  
  Use nohup  



How to configure on Fedora:

1. Install maven 3
<pre>
  sudo yum install maven
</pre>

2. Install pip
<pre>
  sudo yum install python-pip
</pre>

3. Install virtualenv  
<pre>sudo python-pip install virtualenv</pre>  
