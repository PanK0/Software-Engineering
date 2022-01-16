/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sapienza.softeng.complexrestservice;

import com.fasterxml.jackson.jaxrs.json.*;
import java.util.*;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.jaxrs.*;
import org.apache.cxf.jaxrs.lifecycle.*;

/**
 *
 * @author studente
 */
public class Server {

    public static void main(String args[]) throws Exception {

        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(FligthsRepository.class);
        FligthsRepository fr = new FligthsRepository();
        fr.setConnection(args[0]);
        factoryBean.setResourceProvider(new SingletonResourceProvider(fr));
        factoryBean.setAddress("http://localhost:8080/");

        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJaxbJsonProvider());

        factoryBean.setProviders(providers);

        BindingFactoryManager manager = factoryBean.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory restFactory = new JAXRSBindingFactory();
        restFactory.setBus(factoryBean.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, restFactory);

        org.apache.cxf.endpoint.Server server = factoryBean.create();

        System.out.println("Server ready...");

        while (true) {
        }
    }
}
