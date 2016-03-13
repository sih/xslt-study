<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

	<!-- 
		Turns a relatively flat XML structure of resultSets, rows, row, col in to a more structured output of people and their addresses
		@author sih
		@date 2016-03-13
	 -->
	<xsl:template match="//rows">
		<people>
		<!--
			Outer group:
			
			Use the person id to group so we can create a singular person element for each person we encounter
			
			Find all the columns that represent the id
			>> ./row/col/name[text()='id']
			
			Then group by ids that have the same value
			>> group-by="../value/text()
			
			We are matching the <col><name>id</name><value>...</value></col> nodes

		 -->
		<xsl:for-each-group select="./row/col/name[text()='id']" group-by="../value/text()">
            <person>
                <id>
					<xsl:value-of select="current-grouping-key()"/>
				</id>
				<!--  step "back out" here to grab the value of the name -->
            	<name>
            		<xsl:value-of select="../../col[name/text()='name']/value/text()"/>
            	</name>
            <addresses>
            
            	<!-- 
            		Inner group:

					Use the address column but only for the current person id to group by.
					
					We will end up with unique addresses (in real life wouldn't help if someone had moved to and from same address)
            		
            		Need to find the rows that match on the current grouping key, i.e. the id of the person
            		>> //row[col/name/text()='id' and col/value/text()=current-grouping-key()]
            		
            		For these rows we need to get columns that have a name of address
            		>> /col[name/text()='address']
            		
            		And we then need to group address values together
            		>> group-by="value/text()
            		
            		Key point we are at the row element
            	 -->
            
             	<xsl:for-each-group select="//row[col/name/text()='id' and col/value/text()=current-grouping-key()]/col[name/text()='address']" group-by="value/text()">
             		<address>
             			<line1>
             				<xsl:value-of select="current-grouping-key()"/>
             			</line1>
             			<!--  
             				Just like we did above step back to get the postcode
             				
             				But remember we're at the row element so don't need the additional .. as above
             			 -->
             			<postcode>
             				<xsl:value-of select="../col[name/text()='postcode']/value/text()"/>
             			</postcode>
             		</address>
             	</xsl:for-each-group>
            </addresses>
            </person>            
		</xsl:for-each-group>
		</people>
	</xsl:template>
	
</xsl:stylesheet>