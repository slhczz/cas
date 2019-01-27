package org.apereo.cas.services;

import org.apereo.cas.services.changelog.NoOpRegisteredServiceChangelogManager;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * This is {@link AbstractServicesManagerTests}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
public abstract class AbstractServicesManagerTests {
    private static final String TEST = "test";
    protected final List<RegisteredService> listOfDefaultServices = new ArrayList<>();
    protected ServiceRegistry serviceRegistry;
    protected ServicesManager servicesManager;

    public AbstractServicesManagerTests() {
        val r = new RegexRegisteredService();
        r.setId(2500);
        r.setServiceId("serviceId");
        r.setName("serviceName");
        r.setEvaluationOrder(1000);
        listOfDefaultServices.add(r);
    }

    @Before
    public void initialize() {
        this.serviceRegistry = getServiceRegistryInstance();
        this.servicesManager = getServicesManagerInstance();
        this.servicesManager.load();
    }

    protected ServicesManager getServicesManagerInstance() {
        return new DefaultServicesManager(serviceRegistry, mock(ApplicationEventPublisher.class),
            new HashSet<>(),
            new NoOpRegisteredServiceChangelogManager());
    }

    protected ServiceRegistry getServiceRegistryInstance() {
        return new InMemoryServiceRegistry(mock(ApplicationEventPublisher.class), listOfDefaultServices);
    }

    @Test
    public void verifySaveAndGet() {
        val r = new RegexRegisteredService();
        r.setId(1000);
        r.setName(TEST);
        r.setServiceId(TEST);

        this.servicesManager.save(r);
        assertNotNull(this.servicesManager.findServiceBy(1000));
    }

    @Test
    public void verifyDelete() {
        val r = new RegexRegisteredService();
        r.setId(1000);
        r.setName(TEST);
        r.setServiceId(TEST);
        this.servicesManager.save(r);
        assertNotNull(this.servicesManager.findServiceBy(r.getServiceId()));
        this.servicesManager.delete(r);
        assertNull(this.servicesManager.findServiceBy(r.getId()));
    }
}
