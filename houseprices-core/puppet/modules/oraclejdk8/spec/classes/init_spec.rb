require 'spec_helper'
describe 'oraclejdk8' do

  context 'with defaults for all parameters' do
    it { should contain_class('oraclejdk8') }
  end
end
