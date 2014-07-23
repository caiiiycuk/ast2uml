ast2uml
=======

Ast tree to uml converter. Works only for clang + plant uml

Usage
=====

Single file:

```bash
cd ast2uml
sbt one-jar
clang -cc1 -ast-dump file | java -jar target/scala-2.10/ast2uml_2.10-0.1-SNAPSHOT-one-jar.jar > plant.uml
```

Multiple files:

```bash
find folder -name '*.c' -o -name '*.cpp' -o -name '*.h' -o -name '*.hpp' | xargs clang -cc1 -ast-dump | java -jar target/scala-2.10/ast2uml_2.10-0.1-SNAPSHOT-one-jar.jar > plant.uml
```

Use http://www.plantuml.com/plantuml/form to render plant files.

