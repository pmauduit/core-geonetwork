package org.fao.geonet.repository;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fao.geonet.domain.Metadata;
import org.fao.geonet.domain.MetadataCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@Ignore("This integration test requires a real PostGreSQL database.")
@ContextConfiguration(locations = { "classpath:issue1876-context.xml" })
public class MetadataRepositoryIssue1876IntegrationTest {

    @Autowired
    MetadataRepository mdRepo;

    @PersistenceContext
    EntityManager _entityManager;

    public void setUp() throws UnknownHostException, IOException {

    }

    /**
     * Test metadata retrieval.
     * 
     * Before the modifications related to issue #1876, the following timeout
     * needed to be set ~ 13000 ms. 1s should suffice after JPA annotations
     * modifications on Metadta / MetadataCategory for the whole test to be
     * executed.
     * 
     */
    @Test(timeout = 500)
    public void testSelectMetadata() {
        Metadata m = mdRepo.findOne(16667);
        Set<MetadataCategory> d = m.getMetadataCategories();

        Assert.assertNotNull("Metadata is null", m);
    }
}
