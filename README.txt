//////////////////////////////////////////////

DLSimulator

/////////////////////////////////////////////

SetUp linux Box
================

Linux is setup with patinoa user
This user has /home/caf as a home folder
In order to setup ROOT directory a jail setup was established n
ssh/sshd_config:

#Match User anoncvs
#       X11Forwarding no
#       AllowTcpForwarding no
#       PermitTTY no
#       ForceCommand cvs server
Match User patinoa
          ChrootDirectory /home/jail
          X11Forwarding no
          AllowTcpForwarding no
#         ForceCommand internal-ssh
          ForceCommand internal-sftp

In order to access root directory setuid to root for user patinoa


SetUp Java
================

Project is in /home/apatino/workspace_java/DLSimulator
Run packaged project from command line with root access:

[13:38][apatino@m4800:~]$ sudo java -jar DLSimulator.jar 
register: /home/jail
ENTRY_CREATE: /home/jail/LINKS1.req
ENTRY_CREATE: /home/jail/LINKS1.out
ENTRY_CREATE: /home/jail/LINKS1.out.gz


Open eclipse project to crypt out file you get from client:
BBGDLCrypt

1 vim test1.req
2 run crypt program
3 sudo cp XX3_1761542533.out.gz /home/jail
4 mv test1.out.gz XX3_1761542533.out.gz

