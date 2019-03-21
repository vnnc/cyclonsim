#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import matplotlib.pyplot as plt, csv, pandas, numpy

if len(sys.argv) == 1:
    filename = input("Fichier: ")
    annexe = input("Annexe: ")
else:
    filename = sys.argv[1]
    annexe = sys.argv[2]
    
filename = str(filename)
annexe = str(annexe)

results_data = pandas.read_csv(filename, sep=",")

shuffles = results_data["SHUFFLE_INTERVAL"].tolist()

distritheo = results_data["KHI2_DISTRIB_THEO"].tolist()

indeptheo = results_data["KHI2_INDEP_THEO"].tolist()

annexe_data = pandas.read_csv(annexe,sep=",")

dist_values = []
indep_values = []

fig,ax = plt.subplots(figsize=(10,10))
thlimit = ax.plot(shuffles,distritheo,"g",label="Valeur limite du test")
boxes = annexe_data.boxplot(column="DIST_VALUE",by="SHUFFLE_INTERVAL",ax=ax)
plt.suptitle("")
plt.legend()
plt.title("Test de distribution")
plt.xlabel("Nombre de shuffle")
plt.ylabel("Valeur statistique")
plt.show()

#plt.plot(shuffles,indeptheo,"g")
#annexe_data.boxplot(column="INDEP_VALUE",by="SHUFFLE_INTERVAL")
#plt.title("Test d'indépendance")
#plt.xlabel("Nombre de shuffle")
#plt.ylabel("Valeur statistique")
#plt.show()
