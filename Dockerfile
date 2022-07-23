# This image include almost all software used during ysyx project.
# run command `docker image build -t ysyx-env --build-arg jobs=3 <path to this file> ` 
# to create docker image by yourself `jobs` specify the max job number to compile the verilator.
# or run command `docker pull fenglinger/ysyx-env`
# to downloat the pre-built image from docker hub.
# use command 
# `docker run -it --name ysyx-container -v <path ot your ysyx project>:/home/ysyx-project fenglinger/ysyx-env`
# to create a container immediately.

FROM ubuntu:22.04

LABEL Author XinlongWu
LABEL Email vincenttttwu@gmail.com

# base software
RUN if `arch` =~ "x86";then REPO="ubuntu"; else REPO="ubuntu-ports"; fi &&\
    # echo "deb http://mirrors.tuna.tsinghua.edu.cn/$REPO/ jammy main restricted universe multiverse \n\
    #     deb http://mirrors.tuna.tsinghua.edu.cn/$REPO/ jammy-updates main restricted universe multiverse \n\
    #     deb http://mirrors.tuna.tsinghua.edu.cn/$REPO/ jammy-backports main restricted universe multiverse \n\
    #     deb http://mirrors.tuna.tsinghua.edu.cn/$REPO/ jammy-security main restricted universe multiverse \n" > /etc/apt/sources.list &&\
    apt-get clean &&\
    apt-get update &&\
    apt-get install -y vim

# PA related software
RUN apt-get install -y sudo build-essential man gcc-doc gdb git libreadline-dev libsdl2-dev libsdl2-image-dev \
    llvm tmux autoconf flex bison gcc-riscv64-linux-gnu openjdk-17-jdk curl 

RUN sh -c "curl -L https://github.com/com-lihaoyi/mill/releases/download/0.10.5/0.10.5 \
            > /usr/local/bin/mill && chmod +x /usr/local/bin/mill"

# verilator
# change max job number default = 3
ARG jobs=3
ENV VERILATOR_ROOT=/verilator
RUN git clone -b v4.210 https://github.com/verilator/verilator.git &&\
    cd verilator &&\
    autoupdate && autoconf &&\
    ./configure && make -j ${jobs}; exit 0  && sh -c "make install" &&\
    verilator --version && verilator_bin --version

RUN adduser ysyx-project
WORKDIR /home/ysyx-project
USER ysyx-project

ENV YSYX_HOME=/home/ysyx-project
ENV NEMU_HOME = $(YSYX_HOME)/nemu
ENV NVBOARD_HOME=$(YSYX_HOME)/nvboard
ENV AM_HOME=$(YSYX_HOME)/abstract-machine
ENV PATH=/verilator/bin:${PATH}