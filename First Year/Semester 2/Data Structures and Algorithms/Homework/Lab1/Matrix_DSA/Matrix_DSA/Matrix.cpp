#include "Matrix.h"
#include "MatrixIterator.h"
#include <exception>
using namespace std;


Matrix::Matrix(int nrLines, int nrCols) {
	   
	//TODO - Implementation
	if (nrLines <= 0 || nrCols <= 0)
		throw exception();
	this->capacity = 1;
	this->matrix = new Triple[capacity];
	this->nrLin = nrLines;
	this->nrCol = nrCols;
	this->matrix_size = 0;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


int Matrix::nrLines() const {
	//TODO - Implementation
	return this->nrLin;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


int Matrix::nrColumns() const {
	//TODO - Implementation
	return this->nrCol;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


TElem Matrix::element(int i, int j) const {
	//TODO - Implementation
	if (i < 0 || j < 0 || i >= this->nrLin || j >= this->nrCol)
		throw exception();
	for (int k = 0; k < this->matrix_size; k++)
		if (get<0>(this->matrix[k]) == i && get<1>(this->matrix[k]) == j)
			return get<2>(this->matrix[k]);
	return NULL_TELEM;
}
// BC: Theta(1) - when the element is on the first position in the matrix
// WC: Theta(matrix_size) - when the element is on the last position in the matrix or it is not in the matrix
// TC: O(matrix_size)

TElem Matrix::modify(int i, int j, TElem e)
{
	//TODO - Implementation
	if (i < 0 || j < 0 || i >= this->nrLin || j >= this->nrCol)
		throw exception();

	int old=0,poz=0;

	for (int k = 0; k < this->matrix_size; k++)
		if (get<0>(this->matrix[k]) == i && get<1>(this->matrix[k]) == j)
		{
			old = get<2>(this->matrix[k]);
			poz = k;
		}

	if (e != NULL_TELEM)
	{
		if (old != NULL_TELEM)
		{	
			get<2>(this->matrix[poz]) = e;
			return old;
		}

		else
		{
			int find=0;
			while ((get<0>(matrix[find]) < i || (get<0>(matrix[find]) == i && get<1>(matrix[find]) == j)) && find < this->matrix_size)
				find++;

			if (this->matrix_size == this->capacity)
				resize();
			for(int k=this->matrix_size;k>find;k--)
				this->matrix[k] = this->matrix[k-1];
			this->matrix[find] = make_tuple(i, j, e);
			this->matrix_size++;
			return NULL_TELEM;
		}
	}
	else
	{

		if (old != NULL_TELEM)
		{
		
			for (int l = poz; l < this->matrix_size - 1; l++)
				this->matrix[l] = this->matrix[l + 1];
			this->matrix_size--;
			return old;
				
		}

		else
			return NULL_TELEM;
		
	}
	return NULL_TELEM;
}
// BC: Theta(1) - when the element is on the first position in the matrix and it is modified with another element or when the element is not in the matrix and it is modified with NULL_TELEM
// WC: Theta(matrix_size) - when the element is on the last position in the matrix or it is not in the matrix
// TC: O(matrix_size)


Matrix::~Matrix() {
	//TODO - Implementation
	delete[] this->matrix;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void Matrix::resize() {
	Triple* new_matrix = new Triple[this->capacity * 2];
	for (int i = 0; i < this->matrix_size; i++)
		new_matrix[i] = this->matrix[i];
	delete[] this->matrix;
	this->matrix = new_matrix;
	this->capacity *= 2;
}
// BC: Theta(matrix_size) WC: Theta(matrix_size) TC: Theta(matrix_size)

MatrixIterator Matrix::iterator() const {
	return MatrixIterator(*this);
}

