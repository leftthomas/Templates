#!/usr/bin/env python
# Copyright (c) Hao Ren.

import pickle as pkl
import sys
import torch

"""
Usage:
  # download one of the swin_{t,s,b}22k models from:
  https://github.com/microsoft/Swin-Transformer
  # run the conversion
  ./swin-to-d2.py swinb.pth swinb.pkl
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
    for k in list(obj['model'].keys()):
        if 'attn_mask' not in k:
            newmodel[k] = obj['model'].pop(k).detach().numpy()

    res = {"model": newmodel, "__author__": "torchvision", "matching_heuristics": True}

    with open(sys.argv[2], "wb") as f:
        pkl.dump(res, f)
    if obj:
        print("Unconverted keys:", obj['model'].keys())
