include oraclejdk8
include elasticsearch

Firewall {
  before  => Class['my_fw::post'],
  require => Class['my_fw::pre'],
}

class { ['my_fw::pre', 'my_fw::post']: }
class { 'firewall': }

firewall { '009 ElasticSearch':
  dport => 9200,
  proto => tcp,
  action => accept
}
