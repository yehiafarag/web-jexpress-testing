/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.server.listeners;

import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import web.diva.server.model.Computing;

/**
 *
 * @author Yehia Farag
 */
public class ContexListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Computing comp = new Computing();
        String path = sce.getServletContext().getInitParameter("fileFolder");
        comp.initDivaDatasets(path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String path = sce.getServletContext().getInitParameter("fileFolder");
          File appFolder = new File(path);
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().endsWith(".png")) {             
                f2.delete();
            }
                
        }
        System.out.println("good bye :-)");
    }
    
}
