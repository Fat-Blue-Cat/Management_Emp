package com.ncc.manage_emp;


import com.ncc.manage_emp.config_profile_demo.*;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.model_example.Prototype;
import com.ncc.manage_emp.model_example.SingleTon;
import com.ncc.manage_emp.model_example.UserSession;
import com.ncc.manage_emp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Optional;


@SpringBootApplication
@EnableCaching // CONFIG CACHE
@EnableScheduling // CONFIG SCHEDULE
@EnableAsync // CONFIG FOR EVENT ASYNC
@PropertySources({
		@PropertySource("classpath:config1.properties"),
		@PropertySource("classpath:config2.properties"),
})
// CONFIG WITHOUT @Configuration
@ConfigurationPropertiesScan("com.ncc.manage_emp.config_profile_demo")
public class ManageEmpApplication  implements CommandLineRunner {

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return new ModelMapper();
	}


	public static void main(String[] args)  {
		// ====IOC: CREATE, AUTOWIRE, CONFIG AND MANAGE CYCLE. USING DI MANAGE COMPONENT BUILD APP -> SPRING BEAN
		// ====DI: CLASS NOT DIRECT INTERACT PARENT CALL CHILD: INTERFACE. REDUCE DEPENDENCIES MODULES
		SpringApplication.run(ManageEmpApplication.class, args);

//		SpringApplication application = new SpringApplication(ManageEmpApplication.class);
//		ConfigurableEnvironment environment = new StandardEnvironment();
//		environment.setActiveProfiles("dev");
//		application.setEnvironment(environment);
//		application.run(args);


		// ====== DEMO BEAN LIFE  ============
//		==CONTAINER START -> BEAN INIT ->  DEPENCIES INJECT -> INTERNAL SPRING PROCESS
//		== -> +CUSTOM INIT METHOD+ -> BEAN READY/CONTAINER SHUTDOW -> +CUSTOM DESTROY METHOD+

//		ApplicationContext context = SpringApplication.run(ManageEmpApplication.class, args);
//		System.out.println("> After initializing the IoC Container");
//		UserSession userSession = context.getBean(UserSession.class);
//		System.out.println("> Before the IoC Container Destroy userSession");
//		((ConfigurableApplicationContext) context).getBeanFactory().destroyBean(userSession);
//
//		System.out.println("> After the IoC Container Destroy userSession");

	}



	@Value("${my.property.name}")
	private String priority;
	// Value Annotation
	@Value("${app.message1}") // get value with @PropertySource
	public String message1;

	@Value("${app.message2}") // get value with @PropertySource
	public String message2;


	@Autowired //Environment Abstraction
	private Environment env;


	@Autowired // get value with prefix = mail1
	private ConfigProperties configProperties;

	@Autowired
	private ConfigPropertiesWithScan configPropertiesWithScan;

	@Autowired // get value with prefix = mail3
	private ConfigNestedProperties configNestedProperties;

	@Autowired // multiple file
	private YamlConfig yamlConfig;

	@Autowired// list object
	private ConfigListObject configListObject;


////	=========== EXAMPLE SINGLETON AND PROTOTYPE ================
	@Autowired
	private Prototype prototype1;
	@Autowired
	private Prototype prototype2;

	@Autowired
	private SingleTon singleTon1;
	@Autowired
	private SingleTon singleTon2;

//	@Override
	public void run(String... args) throws Exception {
//
		//========Spring Boot Usage==========
		//@PropertySources
		System.out.println("====== GET VALUE WITH PROPERTY SOURCES=======");
		System.out.println(message1);
		System.out.println(message2 + "\n");
		// Environment Abstraction
		System.out.println("====== GET VALUE WITH ENV ABS=======");
		System.out.println(env.getProperty("app.message1") + "\n");
		// @ConfigurationProperties
		System.out.println("====== GET VALUE WITH PREFIX=======");
		System.out.println(configProperties.getHostName());
		System.out.println(configProperties.getFrom());
		System.out.println(configProperties.getPort() + "\n");
		// config with scan
		System.out.println("====== GET VALUE WITH PREFIX +  SCAN=======");
		System.out.println(configPropertiesWithScan.getHostName());
		System.out.println(configPropertiesWithScan.getFrom());
		System.out.println(configPropertiesWithScan.getPort() + "\n");

//         config nested class
		System.out.println("====== GET VALUE WITH PREFIX =======");
		System.out.println("====== GET VALUE WITH NESTED PROPERTIES=======");
		System.out.println("Default Recipients:");
		for (String recipient : configNestedProperties.getDefaultRecipients()) {
			System.out.println("  - " + recipient);
		}

		System.out.println("Additional Headers:");
		for (Map.Entry<String, String> entry : configNestedProperties.getAdditionalHeaders().entrySet()) {
			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
		}

		Credentials credentials = configNestedProperties.getCredentials();
		if (credentials != null) {
			System.out.println("Credentials:");
			System.out.println("  Username: " + credentials.getUsername());
			System.out.println("  Password: " + credentials.getPassword());
			System.out.println("  Auth Method: " + credentials.getAuthMethod());
		}
		System.out.println();


		// Config multiple profile
		System.out.println("====== GET VALUE WITH MULTIPLE FILE =======");
		System.out.println("using environment: " + yamlConfig.getEnvironment());
		System.out.println("name: " + yamlConfig.getName());
		System.out.println("enabled:" + yamlConfig.isEnabled());
		System.out.println("servers: " + yamlConfig.getServers() + "\n");

		// Config with ListObject
		System.out.println("====== GET VALUE WITH LIST OBJECT =======");
		for (Object o : configListObject.getProfiles()) {
			System.out.println(o);
		}

		// Test with priority of application.yml and active file
		System.out.println("=============TEST WITH PRIORITY OF APPLICATION.YML AND ACTIVE FILE USING IDE");
		System.out.println(priority);


//			=========== EXAMPLE SINGLETON AND PROTOTYPE ================
		System.out.println(prototype1 == prototype2);
		System.out.println(singleTon1 == singleTon2);


		// =========== EXAMPLE FETCH LAZY AND EAGER ======================
//		findUserById();

	}
	@Autowired
	private UserRepository userRepository;

	private void findUserById() {

		Long theId = 2l;
		Optional<Users> users = userRepository.findById(theId);

		System.out.println("users: " + users.get());
		System.out.println("the associated timeloglist: " + users.get().getTimeLogsList());

		System.out.println("Done!");
	}
}


