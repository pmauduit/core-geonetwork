package org.fao.geonet.services.statistics;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.fao.geonet.domain.ISODate;
import org.fao.geonet.domain.statistic.SearchRequest;
import org.fao.geonet.domain.statistic.SearchRequestParam;
import org.fao.geonet.repository.statistic.SearchRequestRepository;
import org.fao.geonet.services.AbstractServiceIntegrationTest;
import org.fao.geonet.services.statistics.response.GeneralSearchStats;
import org.fao.geonet.services.statistics.response.IpStats;
import org.fao.geonet.services.statistics.response.SearchTypeStats;
import org.fao.geonet.services.statistics.response.TermFieldStats;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class SearchStatisticsTest extends AbstractServiceIntegrationTest {

    @Autowired
    private SearchRequestRepository requestRepository;
    @Autowired
    private SearchStatistics searchStatistics;

    private SearchRequest waterSearch;
    private SearchRequest borderSearch;
    private SearchRequest capitalSearch;
    private SearchRequest ownerSearch;

    @Test
    public void testGeneralStats() throws Exception {
        addTestData();
        final GeneralSearchStats stats = searchStatistics.generalSearchStats("q");

        assertEquals(31, stats.getActivityDays());
        assertEquals(1, stats.getActivityMonths());
        assertEquals(3, stats.getTotalSearches());
        assertEquals(0, stats.getAvgViewsPerDay());
        assertEquals(0, stats.getAvgViewsPerMonth());
        assertEquals(0, stats.getAvgSearchesPerDay());
        assertEquals(0, stats.getSearchesWithNoHits());
        assertEquals(3, stats.getAvgSearchesPerMonth());
    }

    @Test
    public void testSearchIpStats() throws Exception {
        addTestData();

        List<IpStats> objects = searchStatistics.searchIpStats();

        assertEquals(2, objects.size());
        assertEquals("127.0.0.1", objects.get(0).getIp());
        assertEquals(5L, objects.get(0).getSumhit());
        assertEquals("127.0.0.2", objects.get(1).getIp());
        assertEquals(1L, objects.get(1).getSumhit());
    }

    @Test
    public void testSearchServiceTypeStats() throws Exception {
        addTestData();

        List<SearchTypeStats> objects = searchStatistics.searchServiceTypeStats();

        assertEquals(2, objects.size());
        assertEquals("q", objects.get(0).getService());
        assertEquals(3L, objects.get(0).getNbsearch());
        assertEquals("csw", objects.get(1).getService());
        assertEquals(1L, objects.get(1).getNbsearch());
    }

    @Test
    public void testSearchTermStats() throws Exception {
        addTestData();

        List<TermFieldStats> objects = searchStatistics.searchFieldsStats();

        assertEquals(3, objects.size());

        assertEquals("csw", objects.get(0).getService());
        assertEquals(1L, objects.get(0).getTotal());
        assertEquals("all", objects.get(0).getTermfield());

        assertEquals("q", objects.get(1).getService());
        assertEquals(2L, objects.get(1).getTotal());
        assertEquals("title", objects.get(1).getTermfield());

        assertEquals("q", objects.get(2).getService());
        assertEquals(1L, objects.get(2).getTotal());
        assertEquals("_owner", objects.get(2).getTermfield());
    }

    private void addTestData() {
        waterSearch = new SearchRequest().setAutogenerated(false).setHits(2).setIpAddress("127.0.0.1").
                setLang("eng").setLuceneQuery("+all:water").setSimple(true).setService("csw").setRequestDate(getDate(-2));
        waterSearch.addParam(new SearchRequestParam().setTermField("all").setTermText("water"));
        waterSearch = requestRepository.save(waterSearch);

        borderSearch = new SearchRequest().setAutogenerated(false).setHits(1).setIpAddress("127.0.0.2").
                setLang("eng").setLuceneQuery("+title:border").setSimple(true).setService("q").setRequestDate(getDate(-32));
        borderSearch.addParam(new SearchRequestParam().setTermField("title").setTermText("border"));
        borderSearch = requestRepository.save(borderSearch);

        capitalSearch = new SearchRequest().setAutogenerated(false).setHits(3).setIpAddress("127.0.0.1").
                setLang("eng").setLuceneQuery("+title:capital").setSimple(true).setService("q").setRequestDate(getDate(-1));
        capitalSearch.addParam(new SearchRequestParam().setTermField("title").setTermText("capital"));
        capitalSearch = requestRepository.save(capitalSearch);

        ownerSearch = new SearchRequest().setAutogenerated(true).setHits(5).setIpAddress("127.0.0.0").
                setLang("eng").setLuceneQuery("+_owner:1").setSimple(true).setService("q").setRequestDate(getDate(-1));
        ownerSearch.addParam(new SearchRequestParam().setTermField("_owner").setTermText("1"));
        ownerSearch = requestRepository.save(ownerSearch);

    }

    private ISODate getDate(int days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return new ISODate(calendar.getTimeInMillis(), false);
    }
}