class oraclejdk8 ($rev=66, $build=17) {

  notice("\n\nFetching RPM file from Oracle. \nThis may take some time! Wait for further output...\n")

  exec { "fetch rpm from oracle":
    command => "curl -v -j -k -L -H 'Cookie: oraclelicense=accept-securebackup-cookie' -o /tmp/oraclejdk8-$rev.rpm http://download.oracle.com/otn-pub/java/jdk/8u$rev-b$build/jdk-8u66-linux-x64.rpm",
    path => [ "/usr/bin/"],
    unless => ["test -f /tmp/oraclejdk8-$rev.rpm"]
  }

  package { "erase jdk": 
    ensure => absent,
    provider => rpm,
    name => "jdk1.8.0_$rev"
  }
  ~>
  package { "install from jdk rpm file":
    ensure => present,
    provider => rpm,
    source => "/tmp/oraclejdk8-$rev.rpm",
    install_options => ["-v"]    
  } 
}

