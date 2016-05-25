# setup for py2exe by Nancy Schorr
import sys

from distutils.core import setup
import py2exe

                                            #data_files=[]

setup ( 
                                            #name = 'E:\Workspace\astrofiles\astfiles.py', 
       console = ['astfiles.py']
                                               #name=["E:astrofiles"]
                                               #options = {'includes':['sys','glob', 'os', 'datetime']}
                  )