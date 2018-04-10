import jenkins.model.*;
import hudson.tasks.Shell;
import javaposse.jobdsl.plugin.*;
import org.jenkinsci.plugins.workflow.libs.*
import hudson.scm.SCM;
import hudson.plugins.git.*;
import jenkins.plugins.git.GitSCMSource;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import java.util.Base64;

	// Fetch values from Environment Variables
	def shared_library_url 		= System.getenv("SHARED_LIBRARY_URL")
	def shared_library_version 	= System.getenv("SHARED_LIBRARY_VERSION")
	def shared_library_name 	= System.getenv("SHARED_LIBRARY_NAME")
	def inst 					= Jenkins.getInstance()
	def desc 					= inst.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")
	def home_dir 				= System.getenv("JENKINS_CONF")
	def file 					= new File("$home_dir/simpleci.conf")

	// Get the out variable
	def config = new HashMap()
	def bindings = getBinding()
	config.putAll(bindings.getVariables())
	def out = config['out']
	int i=1;
	
	// check if the file exists at the location. if not wait for it for 2 minutes. Repear this process for 5 times.	
	while(!file.exists() && i<6){
		out.println ("Waiting for SimpleCI.conf file : Iteration " +i)
		i++;
		Thread.sleep(120000)
	}
	//if the file not read even after 10 minutes, print the error message else read the file and continue.
	if(i==6){
		out.println ("Config file not present at the location " + home_dir+". Please generate the file and try again later")	
		return;	
	}else{
	
		def properties 				= new ConfigSlurper().parse(new File("$home_dir/simpleci.conf").toURI().toURL())
		
		def descript
		
		// // This is for adding credentials to Jenkins
		// properties.credentials.each() { configName, serverConfig ->
		
			// // Decode password using base64
		// 	byte[] valueDecoded =  Base64.getDecoder().decode(serverConfig.password);
		//	Credentials creds = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,
		//												  serverConfig.credentialsId,
		//												  serverConfig.description,
		//												  serverConfig.username,new String(valueDecoded)
		//												  /*new File(serverConfig.path).text.trim()*/)
		//	descript = serverConfig.credentialsId;
		//	SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), creds)
		// }
		
		//  Below is the template for the same
		// credentials {
	   // artifactory {
		// 	type 			= "password"
		// 	username		= "nbansal16"
		//	credentialsId 	= "artifactory-publisher"
		//	description 	= "Jenkins-User"
		//	path 			= "/var/jenkins_home/.ssh/.password"
		//	password 		= ""
		//}	
	// }

		
		

		if(shared_library_url!=null && shared_library_version!=null && shared_library_name!=null ){
			
			//This is for Modern SCM as Retrieval Method
			SCMSourceRetriever retriever = new SCMSourceRetriever(new GitSCMSource(
				"scmSourceId",
				shared_library_url,
				descript,
				"*",
				"",
				false))
			
			def name = shared_library_name  
			LibraryConfiguration libconfig = new LibraryConfiguration(name, retriever)
			libconfig.setDefaultVersion(shared_library_version)
			libconfig.setImplicit(true)
			libconfig.setAllowVersionOverride(false)
			desc.get().setLibraries([libconfig])
		}
		//In absence of env_var, look into property file for configurations
		else{
		
			properties.sharedLibrary.each() { configName, serverConfig ->
				// This is for Modern SCM as Retrieval Method
				SCMSourceRetriever retriever = new SCMSourceRetriever(new GitSCMSource(
					"scmSourceId",
					serverConfig.githubURL,
					descript,
					"*",
					"",
					false))
				
				def name = serverConfig.libraryName  
				LibraryConfiguration libconfig = new LibraryConfiguration(name, retriever)
				libconfig.setDefaultVersion(serverConfig.version)
				libconfig.setImplicit(true)
				libconfig.setAllowVersionOverride(false)
				desc.get().setLibraries([libconfig])
			}
		}
	}
