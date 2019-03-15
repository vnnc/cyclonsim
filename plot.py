#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Mar 15 04:41:23 2019

@author: vnnc
"""

import matplotlib.pyplot as plt,csv,pandas,numpy

filename = input("Fichier: ")
data = pandas.read_csv(filename,sep=",")
nbshuffle = data["NOMBRE_SHUFFLE"].tolist()

distriobs = data["KHI2_DISTRIBUTION_CALC"].tolist()
distritheo = data["KHI2_DISTRIBUTION_THEO"].tolist()

indepobs = data["KHI2_INDEP_CALC"].tolist()
indeptheo = data["KHI2_INDEP_THEO"].tolist()

plt.plot(nbshuffle,distritheo,"g")
plt.plot(nbshuffle,distriobs,"r^")
plt.xlabel("Nombre de shuffle")
plt.ylabel("Valeur statistique")
plt.show()

plt.plot(nbshuffle,indeptheo,"g")
plt.plot(nbshuffle,indepobs,"r^")
plt.xlabel("Nombre de shuffle")
plt.ylabel("Valeur statistique")
plt.show()