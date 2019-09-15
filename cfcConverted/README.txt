introduction


the cystic fibrosis database cf consists of seven files

	cf74  cf79  1239 documents published from 1974 to 1979
			 discussing cystic fibrosis aspects

	cfquery       a set of 100 queries with the respective 
			 relevant documents as answers



document files


these are cf74 cf75 cf76 cf77 cf78 and cf79

each document includes 11 fields as follows

  pn	paper number the first two digits give the year of publication 
	and the rest three digits range from 1 to the number of docs 
	published that year

  rn	record number serial id number varying from 1 to 1239

  an    medline acession number cf is a subset of the medline database

  au    authors

  ti    title

  so    source bibliographic citation of source

  mj    major subjects the medical subject headings mesh and subheadings
	representing the major subjects of the document the medical subject
	headings are shown in capital letters and have been assigned by 
	expert indexers the twoletter symbols are subject subheadings also
	assigned manually from a controlled vocabulary see the mesh vocabulary
	published by the national library of medicine  

  mn    minor subjects the medical subject headings mesh and subheadings
  	representing the minor subjects of the document the medical subject
	headings are shown in capital letters and have been assigned by
	expert indexers the twoletter symbols are subject subheadings also
	assigned manually from a controlled vocabulary see the mesh vocabulary
	published by the national library of medicine
	
 abex  abstractextract the abstract of the document or in the case of a
	document with no published abstract an extract from the text

  rf 	references the complete list of references appearing in the document
	excluding private comunications and unpublished documents

  ct	citations a comprehensive list of citations to the document as indexed
	in the scisearchdiaolg files


query file


it is cfquery

each query includes 4 fields

  qn	query number

  qu    query text

  nr	number of relevant documents

  rd    relevant docs and relevant scores the record number rn of each
	document is followed by the relevance scores from 4 different sources
	rew one of the authors faculty colleagues of rew postdoctorate
	associate of rew and jbw other author and a medical bibliographer
	the relevance scores vary from 0 to 2 with the following meaning

		2   highly relevant
		1   marginally relevant
		0   not relevant

        example of a document in rd

		513 0010  

		 doc number 513
	             rel score by rew not relevant
		     rel score by rew colleagues not relevant
		     rel score by rew postdoctorates marginally relevant
		     rel score by jbw not relevant



