class elasticsearch($clusterName = "dwpelasticsearch") {

  # install es signing key
  exec { 'import key':
    command => "rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch",
    path => [ "/usr/bin/", "/bin/" ]
  }

}
