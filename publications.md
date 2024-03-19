---
title:
    "GROOVE publications"
summary:
    "Published papers on GROOVE, together with the rule systems used there"
permalink:
    publications.html
sidebar:
    home_sidebar
toc: 
    false
last_updated:
    false
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
tags:      # need to be included in _data/tags_doc.yml and have a page in tags/
keywords:  # used in metadata for findability
---

**Note:** The rule systems below are frozen after uploading. This means that they may correspond to an older GROOVE version, and may need conversion before loading.

- __Formal Definition of a General Ontology Pattern Language Using a Graph Grammar__ ([FedCSIS 2017](https://annals-csis.org/Volume_11/drp/pdf/001.pdf))

  Rule system used in the paper: [Ontology Pattern Grammar](grammars/onto-pattern-final-with-user-input.gps.zip)

- __How Much are your Geraniums? Taking Graph Conditions beyond First Order__ ([ModelEd, TestEd, TrustEd 2017](https://link.springer.com/chapter/10.1007/978-3-319-68270-9_10))

  Rule system used in the paper: [Geraniums experiment](grammars/full-experiment.gps.zip)

- __A Tutorial on Graph Transformation__ ([Graph Transformation, Specifications, and Nets 2017](https://link.springer.com/chapter/10.1007/978-3-319-75396-6_5))

  Rule system used in the paper: [Leader Election Protocol](grammars/leader-election.gps.zip)

- __Graph Subsumption in Abstract State Space Exploration__ ([GRAPHITE 2012](https://arxiv.org/abs/1210.6413))

  Rule systems used in the paper: [ZIP with all the rule systems](grammars/graphite_grammars.zip)

- __Knowledge-based Graph Exploration Analysis__ ([AGTIVE 2011](https://link.springer.com/chapter/10.1007/978-3-642-34176-2_11))

  Rule system used in the paper: [Feature Model](grammars/feature-model-prolog.gps.zip): grammar encoding a problem from the feature model domain and showing the use of the new Prolog extension.

- __Transformation Tool Contest__ (2011)

  GROOVE submissions to TTC, as pdf and GROOVE grammars:
  
  * [Hello World](grammars/hello-case-description.pdf) case: the [submitted paper](grammars/ttc2011_submission_13.pdf), [greeting rules](grammars/ttc-greeting.gps.zip) and [other rules](grammars/ttc-graph.gps.zip)
  
  * [Compiler optimisation](grammars/compiler-case-description.pdf) case: the [submitted paper](grammars/ttc2011_submission_12.pdf), [task 1](grammars/compiler-task1.gps.zip) rules and [task 2](grammars/compiler-task2.gps.zip) rules

- __Modelling and analysis using GROOVE__ ([STTT 2011](http://dx.doi.org/10.1007/s10009-011-0186-x)); digital version [here](grammars/sttt2011.pdf)

  Rule systems used in the paper:
  * [AntWorld](grammars/brave-new-antworld.gps.zip): grammar encoding a particular model of ant behaviour
  * [BPEL to BPMN](grammars/bpmn_2_bpel.gps.zip): encoding of a model transformation from BPEL to BPMN
  * [Leader Election](grammars/le.gps.zip): specification of a leader election protocol
  * [Secturity Analysis](grammars/sec.gps.zip): grammar allowing the analysis of security policies

- __Isomorphism Checking for Symmetry Reduction__ ([Technical Report, 2010](http://eprints.eemcs.utwente.nl/18117))

  Rule system used in the paper: [No-hops gossiping network](grammars/no-hops.gps.zip) (model of a gossiping network with push and pull modes).

- __Applying Formal Methods to Gossiping Networks with mCRL and Groove__ ([ACM SIGMETRICS 2008](http://dx.doi.org/10.1145/1481506.1481510), digital version [here](grammars/p7-crouzen.pdf))

  Rule system used in the paper: [No-hops gossiping network](grammars/no-hops.gps.zip) (model of a gossiping network with push and pull modes).

- __Ludo: A Case Study for Graph Transformation Tools__ ([AGTIVE 2007](http://dx.doi.org/10.1007/978-3-540-89020-1_34); digital version [here](grammars/ludo.pdf))

  Rule system used in the paper: [Ludo](grammars/ludo.gps.zip) (GROOVE solution of the Ludo game).

- __Isomorphism Checking in GROOVE__ ([GraBaTs 2006](https://journal.ub.tu-berlin.de/eceasst/article/view/77); digital version [here](grammars/grabats2006.pdf))

  Rule systems used in the paper:
  * [Gossiping Girls](grammars/gossip-priorities.gps.zip): model of the "gossiping girls" problem using rule priorities
  * [Another, improved version](grammars/gossip-nested.gps.zip) of the rule system using nested rules
  * [List append](grammars/append.gps.zip): An encoding of a simple recursive function appending elements to a given list
  * [Mutual exclusion](grammars/mutex.gps.zip): A model of a token-based mutual exclusion algorithm
