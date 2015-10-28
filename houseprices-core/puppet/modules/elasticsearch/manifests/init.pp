class elasticsearch($clusterName = "dwpelasticsearch") {

  exec { "import elasticsearc repo key":
    command => "rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch",
    path => [ "/usr/bin/", "/bin/" ]
  }

  file { "add elasticsearch yum repository file":
    ensure => present,
    path => "/etc/yum.repos.d/elasticsearch.repo",
    source => "puppet:///modules/elasticsearch/yum/elasticsearch.repo",
  }

  package { "elasticsearch":
    ensure => present,
    name => "elasticsearch"
  }

  service { "elasticsearch":
    ensure  => running,
    require => [
      Package["elasticsearch"]
    ]
  }
}
