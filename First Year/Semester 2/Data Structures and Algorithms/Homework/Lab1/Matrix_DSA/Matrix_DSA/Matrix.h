#pragma once
#include <tuple>
#include <utility>
//#include "MatrixIterator.h"
//DO NOT CHANGE THIS PART
typedef int TElem;
#define NULL_TELEM 0

typedef std::tuple <int, int, TElem> Triple;

#define NULL_Mat tuple<int, int, TElem>(-1, -1, NULL_TELEM)

class MatrixIterator;

class Matrix {

	friend class MatrixIterator;

private:
	//TODO - Representation
	Triple *matrix;
	int nrLin, nrCol;
	int capacity;
	int matrix_size;

	void resize();
	
public:
	//constructor
	Matrix(int nrLines, int nrCols);

	//returns the number of lines
	int nrLines() const;

	//returns the number of columns
	int nrColumns() const;

	//returns the element from line i and column j (indexing starts from 0)
	//throws exception if (i,j) is not a valid position in the Matrix
	TElem element(int i, int j) const;

	//modifies the value from line i and column j
	//returns the previous value from the position
	//throws exception if (i,j) is not a valid position in the Matrix
	TElem modify(int i, int j, TElem e);

	//destructor
	~Matrix();

	MatrixIterator iterator() const;

};
