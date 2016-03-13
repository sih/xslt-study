# RDB - XML Transformer Study

## Overview

This study demonstrates how to turn tabular, denormalized data from a relational query in to a structured tree-like output by first converting the result set in to a generic XML format representing rows and columns, and then using XSLT to turn this in to a more semantic and normalized structure.

## Detail

The data is held in two relational tables, ```PERSON``` and ```ADDRESS``` where a person can have many addresses. The goal is to output the information as an XML document that normalizes the data, i.e. we have a person's details and then the addresses where they lived embedded.

The tables are simplified models, i.e. Person[id, name, nationality] and Address[person_id, address_1, postcode].

### Step 1

The data is read out via an SQL query that joins the two tables, and returned as a tabular repeating-group.

```
id, name, nationality, address line 1, postcode
1,Alice,GB,39 Woodland Rd,SE19 1NU
1,Alice,GB,34 Dart Rd,GU14 9PB
1,Alice,GB,39A Dorothy Rd,SW11 2JJ
2,Bob,GB,39A Dorothy Rd,SW11 2JJ
```

An [Extractor](https://github.com/sih/xslt-study/blob/master/src/main/java/eu/waldonia/study/xslt/Extractor.java) turns this data in to a generic collection of ```List<Map<String,Object>>``` where each item in the ```List``` is a row in the repeating group and the ```Map``` holds the column names and their values. 

The study hard-codes the column names in the ```ResultSetExtractor``` but in reality these could be obtained from the ```ResultSetMetadata``` to keep the code generic.

### Step 2

The collection is then turned in to a generic XML representation of a result set by a [TableXmlConverter] (https://github.com/sih/xslt-study/blob/master/src/main/java/eu/waldonia/study/xslt/TableXmlConverter.java). This makes the file in to:

```xml
<resultSet>
  <rows>
    <row>
      <col>
        <name>id</name>
        <value>1</value>
      </col>
      <col>
        <name>name</name>
        <value>Alice</value>
      </col>
      <col>
        <name>address</name>
        <value>39 Woodland Rd</value>
      </col>
      <col>
        <name>postcode</name>
        <value>SE19 1NU</value>
      </col>      
    </row>
    <row>
      <col>
        <name>id</name>
        <value>1</value>
      </col>
      <col>
        <name>name</name>
        <value>Alice</value>
      </col>
      <col>
        <name>address</name>
        <value>34 Dart Rd</value>
      </col>
      <col>
        <name>postcode</name>
        <value>GU14 9PB</value>
      </col>      
    </row>
    <row>
      <col>
        <name>id</name>
        <value>1</value>
      </col>
      <col>
        <name>name</name>
        <value>Alice</value>
      </col>
      <col>
        <name>address</name>
        <value>39A Dorothy Rd</value>
      </col>
      <col>
        <name>postcode</name>
        <value>SW11 2JJ</value>
      </col>      
    </row>
    <row>
      <col>
        <name>id</name>
        <value>2</value>
      </col>
      <col>
        <name>name</name>
        <value>Bob</value>
      </col>
      <col>
        <name>address</name>
        <value>39A Dorothy Rd</value>
      </col>
      <col>
        <name>postcode</name>
        <value>SW11 2JJ</value>
      </col>      
    </row>
  </rows>
</resultSet>

```

### Step 3

An [XSL Transformer](https://github.com/sih/xslt-study/blob/master/src/main/java/eu/waldonia/study/xslt/XslTransformer.java) turns this document in to a more structure semantic model by applying a [stylesheet](https://github.com/sih/xslt-study/blob/master/src/main/resources/xml/group.xslt). This uses the grouping feature of XSLT 2.0 to collect the repeating person details and then create an inner group of address details. The final document looks like:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<people>
  <person>
    <id>1</id>
    <name>Alice</name>
    <addresses>
      <address>
        <line1>39 Woodland Rd</line1>
        <postcode>SE19 1NU</postcode>
      </address>
      <address>
        <line1>34 Dart Rd</line1>
        <postcode>GU14 9PB</postcode>
      </address>
      <address>
        <line1>39A Dorothy Rd</line1>
        <postcode>SW11 2JJ</postcode>
      </address>
    </addresses>
  </person>
  <person>
    <id>2</id>
    <name>Bob</name>
    <addresses>
      <address>
        <line1>39A Dorothy Rd</line1>
        <postcode>SW11 2JJ</postcode>
      </address>
    </addresses>
  </person>
</people>

```

### Libraries

This uses an embedded HSQL database for the data and Saxonica Home Edition for the XSL Transformation.
