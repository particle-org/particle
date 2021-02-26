@echo off
mvn install:install-file -Dfile=E:/opensource/Particle/lib/ParticleRaknet-guard-0.0.1.jar -DgroupId=com.particle -DartifactId=ParticleRouteRaknet -Dversion=0.0.1 -Dpackaging=jar
pause