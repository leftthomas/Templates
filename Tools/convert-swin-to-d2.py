#!/usr/bin/env python
# Copyright (c) Hao Ren.

import pickle as pkl
import sys
import torch
import math

"""
Usage:
  # download one of the swin_{t,s,b} models from torchvision:
  wget https://download.pytorch.org/models/swin_b-68c6b09e.pth -O swinb.pth
  # run the conversion
  ./convert-swin-to-d2.py swinb.pth swinb.pkl
  # Then, use swinb.pkl with the following changes in config:
MODEL:
  WEIGHTS: "/path/to/swinb.pkl"
  PIXEL_MEAN: [123.675, 116.280, 103.530]
  PIXEL_STD: [58.395, 57.120, 57.375]
  SWIN:
    TYPE: "B"
INPUT:
  FORMAT: "RGB"
"""

if __name__ == "__main__":
    input = sys.argv[1]

    obj = torch.load(input, map_location="cpu")

    newmodel = {}
    for k in list(obj.keys()):
        old_k = k
        k = k.replace("features.0.0", "patch_embed.proj")
        k = k.replace("features.0.2", "patch_embed.norm")
        for i, t in enumerate([1, 3, 5, 7]):
            k = k.replace("features.{}".format(t), "layers.{}.blocks".format(i))
        for i, t in enumerate([2, 4, 6]):
            k = k.replace("features.{}".format(t), "layers.{}.downsample".format(i))
        k = k.replace("mlp.0", "mlp.fc1")
        k = k.replace("mlp.3", "mlp.fc2")
        print(old_k, "->", k)
        value = obj.pop(old_k).detach()
        if "relative_position_index" in k:
            size = int(math.sqrt(value.shape[0]))
            value = value.view(size, size)
        newmodel[k] = value.numpy()

    res = {"model": newmodel, "__author__": "torchvision", "matching_heuristics": True}

    with open(sys.argv[2], "wb") as f:
        pkl.dump(res, f)
    if obj:
        print("Unconverted keys:", obj.keys())
