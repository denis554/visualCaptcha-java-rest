# -*- mode: ruby -*-
## See http://docs.vagrantup.com/v2/vagrantfile/
Vagrant.require_version ">= 1.4.0"

BOX_NAME = "visualCaptcha-java-rest"

## Main entry point, 2 = Version of config object
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"

  config.ssh.shell = "bash -c 'BASH_ENV=/etc/profile exec bash'"

  config.vm.define BOX_NAME do |t| end
  
  config.vm.hostname = "#{BOX_NAME}.localdomain"

  ## Configure virtual box provider
  ## VM is modified to have a host CPU execution cap of 50% and 2048MB Memory
  config.vm.provider :virtualbox do |vbox|
    vbox.name = BOX_NAME
    vbox.customize ["modifyvm", :id, "--cpuexecutioncap", "75"]
    vbox.memory = 4096
    #vbox.cpus = 2
  end

  config.vm.provision :shell, :inline => "mkdir -p /var/lib/cloud/instance; touch /var/lib/cloud/instance/locale-check.skip"
  config.vm.provision :shell, :inline => "curl -sSL https://get.docker.com/ubuntu/ | sudo sh"
  config.vm.provision "docker", version: "1.4.1"

  ## First parameter is a path to a directory on the host machine, 2nd mounted directory on guest
  config.vm.synced_folder ".", "/host", disabled: false

  ## CONFIGURE NETWORK PORT FORWARDING AND/OR BRIDGES
  ## http://docs.vagrantup.com/v2/getting-started/networking.html
  config.vm.network :forwarded_port, host: 80, guest: 80
  config.vm.network :forwarded_port, host: 8080, guest: 8080, protocol: "tcp"

  ## run vagrant reload 
end

