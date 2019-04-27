import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from itertools import cycle
from sklearn.cluster import AffinityPropagation
from sklearn.datasets.samples_generator import make_blobs

timeList = ['morning', 'noon', 'afternoon', 'evening', 'night']
absPath = 'C:/Users/Konfuse/Desktop/data/'
outPath = absPath + 'Cluster/'

if not os.path.exists(absPath + 'PartitionCSV'):
	os.mkdir(absPath + 'PartitionCSV')

fix = 0.4
for time in timeList:
    dataFile = absPath + 'PartitionCSV/' + time + '.csv'
    data = pd.read_csv(dataFile)
    data = (data - data.min()) / (data.max() - data.min())
    data['SPEED'] = data['SPEED'].map(lambda x : x * fix)
    
    print('process the ' + time + 'data...')
    model = AffinityPropagation().fit(data)

    center_indices  = model.cluster_centers_indices_
    labels = model.labels_
    num_clusters = len(center_indices)
    print('num of clusters: %s.'%num_clusters)
    
    dataArray = data.values
    dataOrigin = pd.read_csv(dataFile)
    dataOriginArray = dataOrigin.values
    
    for k in range(num_clusters):
        class_members = labels == k
        cluster_center = dataOriginArray[center_indices[k]]
        path = outPath + time + ('/cluster%s.txt' % k)
        with open(path, 'wt') as f:
            for point in dataOriginArray[class_members]:
                f.write('%s,%s,%s'%(point[0], point[1], point[2]))
                f.write('\n')