#!/bin/bash
# set the environment for building the module

export WORKSPACE_HOME=/home/sanjayrally/workspaces/twitter
export TOMCAT_HOME=/home/sanjayrally/software/apache-tomcat-7.0.54
export JAVA_HOME=/home/sanjayrally/software/jdk1.7.0_51
export ANT_HOME=/esp/bea103/modules/org.apache.ant_1.6.5
export ANT_OPTS="-Xmx512m"

#  print the info
echo ::  TOMCAT_HOME    = $TOMCAT_HOME
echo ::  JAVA_HOME     	= $JAVA_HOME
echo ::  ANT_HOME	= $ANT_HOME
echo ::  ANT_OPTS	= $ANT_OPTS

export PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH
