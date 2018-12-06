<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:foaf="http://xmlns.com/foaf/0.1/"
                xmlns:void="http://www.w3.org/TR/void/"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xslUtils="java:org.fao.geonet.util.XslUtil"
                version="1.0">

  <xsl:template match="rdf:RDF">
    <gmd:MD_Metadata 
      xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:wms="http://www.opengis.net/wms"
                 xmlns:gco="http://www.isotc211.org/2005/gco"
                 xmlns:xlink="http://www.w3.org/1999/xlink"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://www.isotc211.org/2005/gmd
     http://www.isotc211.org/2005/gmd/gmd.xsd
     http://www.isotc211.org/2005/gmx
     http://www.isotc211.org/2005/gmx/gmx.xsd
     http://www.isotc211.org/2005/srv
     http://schemas.opengis.net/iso/19139/20060504/srv/srv.xsd">
 
     <gmd:fileIdentifier>
        <gco:CharacterString><xsl:value-of select="./dcat:Dataset/@rdf:about"/></gco:CharacterString>
     </gmd:fileIdentifier>
     <gmd:language>
        <gco:CharacterString>eng</gco:CharacterString>
     </gmd:language>
  <gmd:characterSet>
      <gmd:MD_CharacterSetCode codeList="./resources/codeList.xml#MD_CharacterSetCode" codeListValue="utf8"/>
  </gmd:characterSet>
  <gmd:hierarchyLevel>
      <gmd:MD_ScopeCode codeList="./resources/codeList.xml#MD_ScopeCode" codeListValue="dataset"/>
  </gmd:hierarchyLevel>
  <gmd:contact>
      <gmd:CI_ResponsibleParty>
         <gmd:contactInfo>
            <gmd:CI_Contact>
               <gmd:address>
                  <gmd:CI_Address/>
               </gmd:address>
               <gmd:address>
                  <gmd:CI_Address/>
               </gmd:address>
               <gmd:onlineResource>
                  <gmd:CI_OnlineResource>
                     <gmd:linkage>
                        <gmd:URL><xsl:value-of select="./dct:publisher/foaf:Organization/@rdf:about" /></gmd:URL>
                     </gmd:linkage>
                  </gmd:CI_OnlineResource>
               </gmd:onlineResource>
            </gmd:CI_Contact>
         </gmd:contactInfo>
         <gmd:role>
            <gmd:CI_RoleCode codeList="./resources/codeList.xml#CI_RoleCode" codeListValue="pointOfContact"/>
         </gmd:role>
      </gmd:CI_ResponsibleParty>
  </gmd:contact>
  <gmd:dateStamp>
      <gco:DateTime><xsl:value-of select="./dcat:Dataset/dct:modified/text()" /></gco:DateTime>
  </gmd:dateStamp>
  <gmd:metadataStandardName>
      <gco:CharacterString>ISO 19115:2003/19139</gco:CharacterString>
  </gmd:metadataStandardName>
  <gmd:metadataStandardVersion>
      <gco:CharacterString>1.0</gco:CharacterString>
  </gmd:metadataStandardVersion>
  <gmd:identificationInfo>
      <gmd:MD_DataIdentification>
         <gmd:citation xmlns:ows2="http://www.opengis.net/ows/2.0"
                       xmlns:srv="http://www.isotc211.org/2005/srv"
                       xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0">
            <gmd:CI_Citation>
               <gmd:title>
                  <gco:CharacterString><xsl:value-of select="./dcat:Dataset/dct:title/text()" /></gco:CharacterString>
               </gmd:title>
               <gmd:date>
                  <gmd:CI_Date>
                     <gmd:date>
                        <gco:DateTime><xsl:value-of select="./dcat:Dataset/dct:modified/text()" /></gco:DateTime>
                     </gmd:date>
                     <gmd:dateType>
                        <gmd:CI_DateTypeCode codeList="./resources/codeList.xml#CI_DateTypeCode" codeListValue="revision"/>
                     </gmd:dateType>
                  </gmd:CI_Date>
               </gmd:date>
            </gmd:CI_Citation>
         </gmd:citation>
         <gmd:abstract xmlns:ows2="http://www.opengis.net/ows/2.0"
                       xmlns:srv="http://www.isotc211.org/2005/srv"
                       xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0">
            <gco:CharacterString><xsl:value-of select="./dcat:Dataset/dct:description/text()" /></gco:CharacterString>
         </gmd:abstract>
         <gmd:pointOfContact xmlns:ows2="http://www.opengis.net/ows/2.0"
                             xmlns:srv="http://www.isotc211.org/2005/srv"
                             xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0">
            <gmd:CI_ResponsibleParty>
               <gmd:contactInfo>
                  <gmd:CI_Contact>
                     <gmd:address>
                        <gmd:CI_Address/>
                     </gmd:address>
                     <gmd:address>
                        <gmd:CI_Address/>
                     </gmd:address>
                     <gmd:onlineResource>
                        <gmd:CI_OnlineResource>
                           <gmd:linkage>
                              <gmd:URL><xsl:value-of select="./dct:publisher/foaf:Organization/@rdf:about" /></gmd:URL>
                           </gmd:linkage>
                        </gmd:CI_OnlineResource>
                     </gmd:onlineResource>
                  </gmd:CI_Contact>
               </gmd:contactInfo>
               <gmd:role>
                  <gmd:CI_RoleCode codeList="./resources/codeList.xml#CI_RoleCode" codeListValue="pointOfContact"/>
               </gmd:role>
            </gmd:CI_ResponsibleParty>
         </gmd:pointOfContact>
         <gmd:language xmlns:ows2="http://www.opengis.net/ows/2.0"
                       xmlns:srv="http://www.isotc211.org/2005/srv"
                       xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0"
                       gco:nilReason="missing">
            <gco:CharacterString/>
         </gmd:language>
         <gmd:characterSet xmlns:ows2="http://www.opengis.net/ows/2.0"
                           xmlns:srv="http://www.isotc211.org/2005/srv"
                           xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0">
            <gmd:MD_CharacterSetCode codeList="http://www.isotc211.org/2005/resources/codeList.xml#MD_CharacterSetCode"
                                     codeListValue=""/>
         </gmd:characterSet>
         <gmd:topicCategory xmlns:ows2="http://www.opengis.net/ows/2.0"
                            xmlns:srv="http://www.isotc211.org/2005/srv"
                            xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0">
            <gmd:MD_TopicCategoryCode/>
         </gmd:topicCategory>
         <gmd:extent xmlns:ows2="http://www.opengis.net/ows/2.0"
                     xmlns:srv="http://www.isotc211.org/2005/srv"
                     xmlns:inspire_common="http://inspire.ec.europa.eu/schemas/common/1.0">
            <gmd:EX_Extent>
               <gmd:geographicElement>
                  <gmd:EX_GeographicBoundingBox>
                     <gmd:westBoundLongitude>
                        <gco:Decimal>-180</gco:Decimal>
                     </gmd:westBoundLongitude>
                     <gmd:eastBoundLongitude>
                        <gco:Decimal>180</gco:Decimal>
                     </gmd:eastBoundLongitude>
                     <gmd:southBoundLatitude>
                        <gco:Decimal>-90</gco:Decimal>
                     </gmd:southBoundLatitude>
                     <gmd:northBoundLatitude>
                        <gco:Decimal>90</gco:Decimal>
                     </gmd:northBoundLatitude>
                  </gmd:EX_GeographicBoundingBox>
               </gmd:geographicElement>
            </gmd:EX_Extent>
         </gmd:extent>
      </gmd:MD_DataIdentification>
  </gmd:identificationInfo>
  <gmd:distributionInfo>
      <gmd:MD_Distribution>
         <gmd:distributionFormat>
            <gmd:MD_Format>
               <gmd:name gco:nilReason="missing">
                  <gco:CharacterString/>
               </gmd:name>
               <gmd:version gco:nilReason="missing">
                  <gco:CharacterString/>
               </gmd:version>
            </gmd:MD_Format>
         </gmd:distributionFormat>
         <gmd:transferOptions>
            <gmd:MD_DigitalTransferOptions>

<xsl:for-each select="./dcat:Dataset/dcat:distribution/dcat:Distribution">
               <gmd:onLine>
                  <gmd:CI_OnlineResource>
                     <gmd:linkage>
                        <gmd:URL><xsl:value-of select="./dcat:accessURL/text()" /></gmd:URL>
                     </gmd:linkage>
                     <gmd:protocol>
                        <gco:CharacterString><xsl:value-of select="./dcat:format/text()" /></gco:CharacterString>
                     </gmd:protocol>
                     <gmd:name>
                        <gco:CharacterString></gco:CharacterString>
                     </gmd:name>
                     <gmd:description>
                        <gco:CharacterString></gco:CharacterString>
                     </gmd:description>
                  </gmd:CI_OnlineResource>
               </gmd:onLine>
</xsl:for-each>
            </gmd:MD_DigitalTransferOptions>
         </gmd:transferOptions>
      </gmd:MD_Distribution>
  </gmd:distributionInfo>
  <gmd:dataQualityInfo>
      <gmd:DQ_DataQuality>
         <gmd:scope>
            <gmd:DQ_Scope>
               <gmd:level>
                  <gmd:MD_ScopeCode codeListValue="dataset" codeList="./resources/codeList.xml#MD_ScopeCode"/>
               </gmd:level>
            </gmd:DQ_Scope>
         </gmd:scope>
         <gmd:lineage>
            <gmd:LI_Lineage>
               <gmd:statement gco:nilReason="missing">
                  <gco:CharacterString/>
               </gmd:statement>
            </gmd:LI_Lineage>
         </gmd:lineage>
      </gmd:DQ_DataQuality>
  </gmd:dataQualityInfo>
</gmd:MD_Metadata>
  </xsl:template>                
</xsl:stylesheet>