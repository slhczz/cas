package org.apereo.cas.services;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Default implementation of the {@link ServicesManager} interface.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public class DefaultServicesManager extends AbstractServicesManager {

    private Set<RegisteredService> orderedServices = new ConcurrentSkipListSet<>();

    public DefaultServicesManager(final ServiceRegistry serviceRegistry, final ApplicationEventPublisher eventPublisher,
                                  final Set<String> environments, final RegisteredServiceChangelogManager changelogManager) {
        super(serviceRegistry, eventPublisher, environments, changelogManager);
    }

    @Override
    protected Collection<RegisteredService> getCandidateServicesToMatch(final String serviceId) {
        return this.orderedServices;
    }

    @Override
    protected void deleteInternal(final RegisteredService service) {
        this.orderedServices.remove(service);
    }

    @Override
    protected void saveInternal(final RegisteredService service) {
        this.orderedServices = new ConcurrentSkipListSet<>(getAllServices());
    }

    @Override
    protected void loadInternal() {
        this.orderedServices = new ConcurrentSkipListSet<>(getAllServices());
    }
}
