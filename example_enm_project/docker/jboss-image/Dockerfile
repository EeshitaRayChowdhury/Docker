FROM armdocker.rnd.ericsson.se/proj_oss_releases/enm/jboss-dps-enm

COPY standalone-enm.xml $JBOSS_HOME/standalone/configuration/standalone-enm.xml

# Workaround to make Windows Shared folders to work
RUN yum -y install dos2unix
ONBUILD RUN chmod +x  /opt/ericsson/docker/config/init/scripts/pre-start/*.sh
ONBUILD RUN dos2unix  /opt/ericsson/docker/config/init/scripts/pre-start/*.sh

ENTRYPOINT ["entrypoint.sh"]
CMD ["-IP", "-s", "jboss"]
