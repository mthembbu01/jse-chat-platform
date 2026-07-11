# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.5/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.5/reference/using/devtools.html)

### Mermaid using Junie AI
Select Junie plugin and add the prompt: \
In the directory `/mermaid`, create or update a file named `erd-architecture.mmd` with the following content:
Go and Analyze the JPA entities in the `entities` directory and create a mermaid ERD diagram that describes and shows
the properties and relationships between the JPA entities. Use the mermais entity relationship diagram syntax to represent
the entities, their attributes, and the relationships between them. The diagram should include all relevant details such 
as primary keys, foreign keys, and any other important attributes or relationships that are present in the JPA entities.
Make sure to format the diagram correctly so that it can be rendered properly when viewed.

```mermaid

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

