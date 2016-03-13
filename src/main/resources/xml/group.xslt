<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

	<xsl:template match="//rows">
		<people>
		<!--
			Outer group by the (person) id attribute, i.e. the key with a name of id.
			We actually group by the value (could have just  
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
				<xsl:value-of select="//row[col/name/text()='id' and col/value/text()=current-grouping-key()]/col[name/text()='address']/value/text()"/>            
             -->
             	<xsl:for-each-group select="//row[col/name/text()='id' and col/value/text()=current-grouping-key()]/col[name/text()='address']" group-by="value/text()">
             		<address>
             			<line1>
             				<xsl:value-of select="current-grouping-key()"/>
             			</line1>
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
	
	<xsl:template name="address-group">
		<xsl:param name="person-id"/>
		<xsl:value-of select="$person-id"/>
	</xsl:template>
	
	
	
	
	<xsl:template name="address-group-1">
		<xsl:param name="person-id"/>
		            <xsl:for-each-group select="../row/col[name/text()='id']/value[text() = current-grouping-key()]" group-by="../../col[name='address']/value/text()">
            	<address>
            		<line1>
            		<xsl:value-of select="current-grouping-key()"/>
            		</line1>
            		<postcode>
            		    <xsl:value-of select="../../col[name/text()='postcode']/value/text()"/>
            		</postcode>
            	</address>
            </xsl:for-each-group>
		
	</xsl:template>
	
</xsl:stylesheet>