create table config_info(
  id INTEGER primary key autoincrement,
  service_name VARCHAR(100) default '' not null,
  version VARCHAR(50) not null,
  status TINYINT(2) default 2 not null,
  timeout_config TEXT default '' not null,
  loadbalance_config TEXT default '' not null,
  router_config TEXT default '' not null,
  freq_config TEXT default '' not null,
  published_by INT(11) default 0 not null,
  published_at TIMESTAMP,
  created_by INT(11) default 0 not null,
  updated_by INT(11) default 0,
  created_at TIMESTAMP not null,
  updated_at TIMESTAMP not null
);

