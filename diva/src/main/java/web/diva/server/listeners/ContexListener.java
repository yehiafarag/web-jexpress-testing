/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.server.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import web.diva.server.dal.DB;

/**
 *
 * @author Yehia
 */
public class ContexListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DB database = new DB();
        database.loadDatasets(sce.getServletContext().getInitParameter("fileFolder"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("good bye :-)");
    }
    
}
