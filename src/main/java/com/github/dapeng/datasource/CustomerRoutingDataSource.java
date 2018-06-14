package com.github.dapeng.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author with struy.
 * Create by 2018/6/14 00:17
 * email :yq1724555319@gmail.com
 */

public class CustomerRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSource();
    }
}
