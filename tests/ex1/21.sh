# method renaming -> change method name of sibling classes (go up then down)
java -jar ../../mjavac.jar unmarshal rename method getAge 24 getAgeNew ../../examples/ex1/siblings.java.xml result.xml