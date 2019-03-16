#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import matplotlib.pyplot as plt, csv, pandas, numpy

if len(sys.argv) == 1:
	filename = input("Fichier: ")
else:
	filename = sys.argv[1]
filename = str(filename)

data = pandas.read_csv(filename, sep=",")
nbshuffle = data["NOMBRE_SHUFFLE"].tolist()

distriobs = data["KHI2_DISTRIB_CALC"].tolist()
distritheo = data["KHI2_DISTRIB_THEO"].tolist()

indepobs = data["KHI2_INDEP_CALC"].tolist()
indeptheo = data["KHI2_INDEP_THEO"].tolist()

plt.plot(nbshuffle,distritheo,"g")
plt.plot(nbshuffle,distriobs,"r^")
plt.title("Test de distribution")
plt.xlabel("Nombre de shuffle")
plt.ylabel("Valeur statistique")
plt.show()

plt.plot(nbshuffle,indeptheo,"g")
plt.plot(nbshuffle,indepobs,"r^")
plt.title("Test d'indépendance")
plt.xlabel("Nombre de shuffle")
plt.ylabel("Valeur statistique")
plt.show()
