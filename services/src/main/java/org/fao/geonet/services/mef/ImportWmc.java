package org.fao.geonet.services.mef;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jeeves.server.ServiceConfig;
import jeeves.server.UserSession;
import jeeves.server.context.ServiceContext;

import org.fao.geonet.Util;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.domain.ISODate;
import org.fao.geonet.domain.MetadataType;
import org.fao.geonet.kernel.mef.Importer;
import org.fao.geonet.kernel.setting.SettingManager;
import org.fao.geonet.services.NotInReadOnlyModeService;
import org.fao.geonet.utils.Xml;
import org.jdom.Element;

public class ImportWmc extends NotInReadOnlyModeService {

    private String styleSheetWmc;

    @Override
    public void init(Path appPath, ServiceConfig params) throws Exception {
        super.init(appPath, params);
        this.styleSheetWmc = appPath.toString() + File.separator + Geonet.Path.IMPORT_STYLESHEETS + File.separator
                + "OGCWMC-to-ISO19139.xsl";
    }


    @Override
    public Element serviceSpecificExec(Element params, ServiceContext context)  throws Exception {
        String wmcString = Util.getParam(params, "wmc_string");
        String wmcUrl = Util.getParam(params, "wmc_url");
        String viewerUrl = Util.getParam(params, "viewer_url");

        Map<String,Object> xslParams = new HashMap<String,Object>();
        xslParams.put("viewer_url", viewerUrl);
        xslParams.put("wmc_url", wmcUrl);

        // Should this be configurable ?

        xslParams.put("topic", "location");

        UserSession us = context.getUserSession();

        if (us != null) {
            xslParams.put("currentuser_name", us.getName() + " " + us.getSurname()); 
            xslParams.put("currentuser_mail", us.getEmailAddr());
            xslParams.put("currentuser_org", us.getPrincipal().getOrganisation());
        }

        // 1. JDOMize the string
        Element wmcDoc = Xml.loadString(wmcString, false);
        // 2. Apply XSL (styleSheetWmc)
        Element transformedMd = Xml.transform(wmcDoc, Paths.get(styleSheetWmc), xslParams);

        // 4. Inserts the metadata (does basically the same as the metadata.insert.paste service (see Insert.java)

        String uuid = UUID.randomUUID().toString();
        String uuidAction = Util.getParam(params, Params.UUID_ACTION, Params.NOTHING);
        String date = new ISODate().toString();
        final List<String> id = new ArrayList<String>();
        final List<Element> md = new ArrayList<Element>();
        md.add(transformedMd);

        Importer.importRecord(uuid,
        		uuidAction,
        		md,
        		"iso19139", 0,
        		context.getBean(SettingManager.class).getSiteId(),
        		context.getBean(SettingManager.class).getSiteName(),
        		context, id, date,
                date, "1", MetadataType.METADATA);

        Element result = new Element("uuid");
        result.setText(uuid);

        return result;
    }
}
