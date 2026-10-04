by me & [alexandra](https://github.com/alexandra-murariu)
##### Table of Contents  
- [2023-1 -> Merge sort vector I](#2023-1)  
- [2023-2 -> Merge sort vector II](#2023-2)  
- [2023-3 -> Product of polynomials I](#2023-3)  
- [2023-4 -> Product of polynomials II](#2023-4)  
- [2023-5 -> Scalar Product Vector I](#2023-5)  
- [2023-6 -> Scalar Product Vector II](#2023-6)  
- [2023-7 -> Sum of elements in a vector I](#2023-7)  
- [2023-8 -> Sum of elements in a vector II](#2023-8)  

# 2023-1
Consider the following excerpt from a program that is supposed to merge-sort a vector. The function ```worker()``` is called in all processes except process 0, the function ```product()``` is called from process 0 (and from the places described in this excerpt), the function ```mergeSortLocal()``` sorts the specified vector and the function ```mergeParts()``` merges two sorted adjacent vectors, given the pointer to the first element, the total length and the length of the first vector.
``` cpp
void mergeSort(int* v, int dataSize, int myId, int nrProc) {
  if(nrProc == 1 || dataSize <= 1) {
    mergeSortLocal(v, dataSize);
  } else {
    int halfLen = dataSize / 2;
    int halfProc = nrProc / 2;
    int child = myId + halfProc;
    MPI_Ssend(&halfLen, 1, MPI_INT, child, 1, MPI_COMM_WORLD);
    MPI_Ssend(&halfProc, 1, MPI_INT, child, 2, MPI_COMM_WORLD);
    MPI_Ssend(v, halfSize, MPI_INT, child, 3, MPI_COMM_WORLD);
    mergeSort(v+halfSize, dataSize-halfSize, myId, nrProc-halfProc);
    MPI_Recv(v, halfSize, MPI_INT, child, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    mergeParts(v, dataSize, halfSize);
  }
}

void worker(int myId) {
  MPI_Status status;
  int dataSize, nrProc;
  MPI_Recv(&dataSize, 1, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
  auto parent = status.MPI_SOURCE;
  MPI_Recv(&nrProc, 1, MPI_INT, parent, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
  std::vector v(dataSize);
  MPI_Recv(v.data(), dataSize, MPI_INT, parent, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
  mergeSort(v.data(), dataSize, myId, nrProc);
  MPI_Ssend(v.data(), dataSize, MPI_INT, parent, 4, MPI_COMM_WORLD);
}
``` 

Which one of the following issues are present? Describe the changes needed to solve them? (A, C, D)
- **A. the application can deadlock if the length of the vector is smaller than the number of MPI processes.**
- B. the application can produce a wrong result if the input vector size is not a power of 2.
- **C. some worker processes are not used if the number of processes is not a power of 2.**
- **D. the application can deadlock if the number of processes is not a power of 2.**

## Solution
- A: The application can deadlock if the length of the vector is smaller than the number of MPI processes because at least one process will wait to receive data, and **since MPI_Recv() returns when the receive buffer has been filled with valid data, it will be a blocking call => DEADLOCK.**
	- Solution: delete dataSize <= 1 to be able to send an empty list to processes.
- C + D: If the number of processes is odd at a division, the last process does not receive work and remains blocked in MPI_RECV (). (Answered by prof!!)
 	- Solution: when splitting the work between two processes, each process should be associated with a group. If child = myId + halfProc, then the number of processes available to the child must be (nrProc - nrProc/2), and the parent keeps nrProc/2.

# 2023-2
Consider the following excerpt from a program that is supposed to merge-sort a vector. The function ```worker()``` is called in all processes except process 0, the function ```product()``` is called from process 0 (and from the places described in this excerpt), the function ```mergeSortLocal()``` sorts the specified vector and the function ```mergeParts()``` merges two sorted adjacent vectors, given the pointer to the first element, the total length and the length of the first vector.
``` cpp
void mergeSort(int* v, int dataSize, int myId, int nrProc) {
  if(nrProc == 1) {
    mergeSortLocal(v, dataSize);
  } else {
    int halfLen = dataSize / 2;
    int halfProc = (nrProc+1) / 2;
    int child = myId + halfProc;
    MPI_Ssend(&halfLen, 1, MPI_INT, child, 1, MPI_COMM_WORLD);
    MPI_Ssend(&halfProc, 1, MPI_INT, child, 2, MPI_COMM_WORLD);
    MPI_Ssend(v, halfSize, MPI_INT, child, 3, MPI_COMM_WORLD);
    mergeSort(v+halfSize, halfSize, myId, nrProc-halfProc);
    MPI_Recv(v, halfSize, MPI_INT, child, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    mergeParts(v, dataSize, halfSize);
  }
}

void worker(int myId) {
  MPI_Status status;
  int dataSize, nrProc;
  MPI_Recv(&dataSize, 1, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
  auto parent = status.MPI_SOURCE;
  MPI_Recv(&nrProc, 1, MPI_INT, parent, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
  std::vector v(dataSize);
  MPI_Recv(v.data(), dataSize, MPI_INT, parent, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
  mergeSort(v.data(), dataSize, myId, nrProc);
  MPI_Ssend(v.data(), dataSize, MPI_INT, parent, 4, MPI_COMM_WORLD);
}
``` 

Which one of the following issues are present? Describe the changes needed to solve them?
- A. the application can deadlock if the length of the vector is smaller than the number of MPI processes.
- B. **the application can produce a wrong result if the input vector size is not a power of 2**.
- C. **some worker processes are not used if the number of processes is not a power of 2**.
- D. **the application can deadlock if the number of processes is not a power of 2**.

## Solution
- B: When the size of the input data is not a power of 2, the division is not even, resulting in a part of the data not being sorted properly. **Specifically, if the size of the input data is an odd number, the last element of the input array will not be sorted properly**. 
 	- Solution: we can modify the mergeSort function to mergeSort(v+halfSize, **dataSize-halfSize**, myId, nrProc-halfProc);
- C + D: If the number of processes is odd at a division, the last process does not receive work and remains blocked in MPI_RECV (). (Answered by prof!!)
 	- Solution: when splitting the work between two processes, each process should be associated with a group. If child = myId + halfProc, then the number of processes available to the child must be (nrProc - nrProc/2), and the parent keeps nrProc/2.

# 2023-3
Consider the following excerpt from a program that is supposed to compute the product of two non-zero polynomials. The function ```worker()``` is called in all processes except process 0, the function ```product()``` is called from process 0. The polynomials are represented with coefficient for degree 0 at index 0 in the vector.
``` cpp
void product(int nrProc, std::vector<int> const& p, std::vector<int> const& q, std::vector<int> const&r) {
	int sizes[2]; sizes[0] = p.size(); sizes[1] = q.size();
	MPI_BCast(sizes, 2, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_BCast(const_cast<int*>(p.data()), p.size(), MPI_INT, 0, MPI_COMM_WORLD);
	MPI_BCast(const_cast<int*>(q.data()), q.size(), MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> partRes;
	partProd(0, nrProc, p, q, partRes);
	r.resize(p.size()+q.size()-1);
	MPI_Gather(partRes.data(), partRes.size(), MPI_INT, r.data(), partRes.size(), MPI_INT, 0, MPI_COMM_WORLD);
}

void worker(int myId, int nrProc) {
	int sizes[2];
	MPI_BCast(sizes, 2, MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> p(sizes[0]);
	std::vector<int> p(sizes[1]);
	MPI_BCast(p.data(), p.size(), MPI_INT, 0, MPI_COMM_WORLD);
	MPI_BCast(q.data(), q.size(), MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> r;
	partProd(myId, nrProc, p, q, r);
	MPI_Gather(r.data(), r.size(), MPI_INT, nullptr, 0, MPI_INT, 0, MPI_COMM_WORLD);
}

void partProd(int myId, int nrProc,  std::vector<int> const& p, std::vector<int> const& q, std::vector<int> const&r) {
	int chunkSize = (p.size()+q.size()-1) / nrProc;
	r.resize(chunkSize, 0);
	size_t baseIdx = chunkSize * myId;
	for(int i=0; i<chunkSize; ++i) {
		for(int j=0; j<=i+baseIdx; ++j) {
			if(j<p.size() && i+baseIdx-j<q.size()) {
				r[i] += p[j]*q[i+baseIdx-j];
			}
		}
	}
}
```

Which of the following issues are present if the output degree plus one is not a multiple of the number of MPI processes? Describe the changes needed to solve them.
- A: the application can have memory corruption.
- B: the application can deadlock.
- C: some worker processes are not used.
- D: some coefficients are computed twice.
- **E: some coefficients are not computed at all**.
- F: some coefficients are computed incorrectly.

## Solution
- E: This is because the partProd function divides the work among the MPI processes by splitting the output vector into chunks of equal size. If the total number of coefficients to compute is not evenly divisible by the number of processes, then some coefficients will not be computed by any process.
 	- Solution: when we are at the last process, resize r to fit all the remaining coefficients:
 	```cpp
	int totalSize = p.size() + q.size() - 1;
    int chunkSize = totalSize / nrProc;
 	int remainingSize = totalSize - chunkSize * nrProc;
	if (myId == nrProc - 1) {
        chunkSize += remainingSize;
    }
	```


# 2023-4
Consider the following excerpt from a program that is supposed to compute the product of two non-zero polynomials. The function ```worker()``` is called in all processes except process 0, the function ```product()``` is called from process 0. The polynomials are represented with coefficient for degree 0 at index 0 in the vector.
``` cpp
void product(int nrProc, std::vector<int> const& p, std::vector<int> const& q, std::vector<int> const&r) {
	int sizes[2]; sizes[0] = p.size(); sizes[1] = q.size();
	MPI_BCast(sizes, 2, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_BCast(const_cast<int*>(p.data()), p.size(), MPI_INT, 0, MPI_COMM_WORLD);
	MPI_BCast(const_cast<int*>(q.data()), q.size(), MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> partRes;
	partProd(0, nrProc, p, q, partRes);
	r.resize(p.size()+q.size()-1);
	MPI_Gather(partRes.data(), partRes.size(), MPI_INT, r.data(), partRes.size(), MPI_INT, 0, MPI_COMM_WORLD);
}

void worker(int myId, int nrProc) {
	imetadata[0] = nrProc;
	MPI_BCast(sizes, 2, MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> p(sizes[0]);
	std::vector<int> q(sizes[1]);
	MPI_BCast(p.data(), p.size(), MPI_INT, 0, MPI_COMM_WORLD);
	MPI_BCast(q.data(), q.size(), MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> r;
	partProd(myId, nrProc, p, q, r);
	MPI_Gather(r.data(), r.size(), MPI_INT, nullptr, 0, MPI_INT, 0, MPI_COMM_WORLD);
}

void partProd(int myId, int nrProc,  std::vector<int> const& p, std::vector<int> const& q, std::vector<int> const&r) {
	int chunkSize = (p.size()+q.size()-1+nrProc-1) / nrProc;
	size_t baseIdx = chunkSize * myId;
	r.resize(chunkSize, 0);
	for(int i=0; i<chunkSize; ++i) {
		for(int j=0; j<=i+baseIdx; ++j) {
			if(j<p.size() && i+baseIdx-j<q.size()) {
				r[i] += p[j]*q[i+baseIdx-j];
			}
		}
	}
}
```

Which of the following issues are present if the output degree plus one is not a multiple of the number of MPI processes? Describe the changes needed to solve them.
- A: **the application can have memory corruption**.
- B: the application can deadlock.
- C: some worker processes are not used.
- D: some coefficients are computed twice.
- E: some coefficients are not computed at all.
- F: some coefficients are computed incorrectly.

## Solution
- A: When chunkSize is calculated, it is always rounded up => it leads to MPI_Gather() to "overfill" the allocated destination vector, which leads to memory corruption. 
 	- Solution: when we are at the last process, resize r to fit all the remaining coefficients:
 	```cpp
	int totalSize = p.size() + q.size() - 1;
    int chunkSize = totalSize / nrProc;
 	int remainingSize = totalSize - chunkSize * nrProc;
	if (myId == nrProc - 1) {
        chunkSize += remainingSize;
    }
	```

# 2023-5
Consider the following excerpt from a program that is supposed to compute the scalar product of two vectors of the same length. The function ```worker()``` is called in all processes except process 0, the function ```product()``` is called from process 0.
```cpp
int product(int nrProc, std::vector<int> const& p, std::vector<int> const& q) {
	int chunkSize = p.size() / nrProc;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	std::vector<int> partResults(nrProc);
	partProd(chunkSize, p.data(), q.data(), partResults.data());
	int sum = 0;
	for(int const& v: partResults) sum += v;
	return sum;
}

void worker(int myId, int nrProc) {
	int chunkSize;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	partProd(chunkSize, nullptr, nullptr, nullptr);
}

void partProd(int chunkSize, int const* p, int const* q, int* r) {
	std::vector<int> pp(chunkSize);
	std::vector<int> qq(chunkSize);
	MPI_Scatter(p, chunkSize, MPI_INT, pp.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(q, chunkSize, MPI_INT, qq.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD);
	int sum = 0;
	for(int i=0; i<chunkSize; ++i) {
		sum += pp[i]*qq[i];
	}
	MPI_Gather(r, 1, MPI_INT, &sum, 1, MPI_INT, 0, MPI_COMM_WORLD);
}
```

Which of the following issues are present if the input size plus one is not a multiple of the number of MPI processes? Describe the changes needed to solve them.
- A: the application can have memory corruption.
- B: the application can deadlock.
- C: some worker processes are not used.
- D: some terms are added twice.
- E: **some terms are not added at all**.
- F: the scalar product is incorrectly computed in some other way.

# Solution
- E: if there is not divisible, there will be some leftover elements that cannot be evenly distributed to all worker processes, since the chunksize is rounded down. As a result, some elements will not be processed at all, so their contribution to the final result will be missing. 
	- Solution: to solve this issue, we need to distribute the leftover elements among the worker processes as well. One way to do this is to use MPI_Scatterv instead of MPI_Scatter and to "manually" divide the blocks among the processes.
	``` cpp
	int sendcounts[nrProc]; // the array with the number of elements that each process will send
    int displs[nrProc];  // the array with the displacements
    int res = p.size() % nrProc; // the number of extra iterations
	// calculate the size and the displacement of each process
	int increment = 0;
    for(int processID = 0; processID < nrProc; processID++){
       displs[processID] = increment;
       sendcounts[processID] = (processID + 1 <= res) ? chunkSize + 1 : chunkSize; // assign the extra iterations
       increment += sendcounts[processID];
    }
    int process_size = sendcounts[myId];
    int local_numbers[process_size];
    MPI_Scatterv(p.data(), sendcounts, displs, MPI_INT, pp, process_size, MPI_INT, 0, MPI_COMM_WORLD); 
	```

# 2023-6
Consider the following excerpt from a program that is supposed to compute the scalar product of two vectors of the same length. The function ```worker()``` is called in all processes except process 0, the function ```product()``` is called from process 0.
```cpp
int product(int nrProc, std::vector<int> const& p, std::vector<int> const& q) {
	int chunkSize = (p.size() + nrProc -1) / nrProc;
	std::vector<int> partResults(nrProc);
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	partProd(chunkSize, p.data(), q.data(), partResults.data());
	int sum = 0;
	for(int const& v: partResults) sum += v;
	return sum;
}

void worker(int myId, int nrProc) {
	int chunkSize;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	partProd(chunkSize, nullptr, nullptr, nullptr);
}

void partProd(int chunkSize, int const* p, int const* q, int* r) {
	std::vector<int> pp(chunkSize);
	std::vector<int> qq(chunkSize);
	MPI_Scatter(p, chunkSize, MPI_INT, pp.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(q, chunkSize, MPI_INT, qq.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD);
	int sum = 0;
	for(int i=0; i<chunkSize; ++i) {
		sum += pp[i]*qq[i];
	}
	MPI_Gather(r, 1, MPI_INT, &sum, 1, MPI_INT, 0, MPI_COMM_WORLD);
}
```

Which of the following issues are present if the input size plus one is not a multiple of the number of MPI processes? Describe the changes needed to solve them.
- A: the application can have memory corruption.
- B: the application can deadlock.
- C: some worker processes are not used.
- D: some terms are added twice.
- E: some terms are not added at all.
- F: **the scalar product is incorrectly computed in some other way.**

# Solution
- F: Because the chunksize is rounded up, the ceiling division leads to invalid memory access. 
	- Solution: add padding to the input vectors so that their size becomes a multiple of the number of MPI processes. This can be done by adding zeros to the end of the vectors. The padding can be removed from the final result by subtracting the contribution of the padded zeros
	```cpp
	v.resize(((v.size()+nrProcs-1)/nrProcs)*nrProcs, 0);
	```

# 2023-7
Consider the following excerpt from a program that is supposed to compute the sum of elements in a vector. The function ``worker()`` is called in all processes except process 0, the function ``product()`` is called from process 0.
``` cpp
int product(int nrProc, std::vector<int> const&v) {
	int chunkSize = v.size() / nrProc;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	return sumRec(0, nrProc, chunkSize, v.data());
}

void worker(int myId, int nrProc) {
	int chunkSize;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	sumRec(myId, nrProc, chunkSize, nullptr);
}

int sumRec(int myId, int nrProc, int chunkSize, int const* v) {
	std::vector<int> vv(chunkSize);
	MPI_Scatter(v, chunkSize, MPI_INT, vv.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD);
	int sum = 0
	for(int value : vv) {
		sum += value;
	}
	int t = 0;
	if(2*myId < nrProc) {
		MPI_Recv(&t, 1, MPI_INT, 2*myId, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		sum += t;
	}
	if(2*myId+1 < nrProc) {
		MPI_Recv(&t, 1, MPI_INT, 2*myId+1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		sum += t;
	}
	if(myId != 0) {
		MPI_Send(&sum, 1, MPI_INT, myId/2, 1, MPI_COMM_WORLD);
	}
	return sum;
}
```

Which of the following issues are present if the input size plus one is not a multiple of the number of MPI processes? Describe the changes needed to solve them.
- A: the application can have memory corruption.
- B: **the application can deadlock**.
- C: some worker processes are not used.
- D: some terms are added twice.
- E: **some terms are not added at all**.
- F: the scalar product is incorrectly computed in some other way.

# Solution
- B: Process 0 never sends anything because it is the root of the binary tree(the condition ```if(myId != 0)```). But, the condition for the first if is correct for process 0, and it waits to receive something from itself. Since MPI_Recv() is a blocking call => DEADLOCK. 
	- Solution: this program has the structure of a binary tree but has the wrong formulas for the left and right child. Because the numerotation starts from 0 and not from 1, we should always add 1. So, instead of ```2*myId``` it should be ```2*myId+1``` for the left child, and instead of ```2*myId+1``` it should be ```2*myId+2``` for the right child. Also, when a process sends to another process, it should send to ```(myId-1)/2```.
	
	![image](https://user-images.githubusercontent.com/53339016/220913913-95c88125-07f8-4164-8e33-6204a301aeb5.png)
- E: Because the chunkSize is rounded down, there will be some elements left over that are not included in the computation.
	- Solution: have the last process handle the remaining elements.
	``` cpp
	// after MPI_Scatter
    if (myId == nrProc - 1) {
		int remainder = v.size() % nrProc;
        chunkSize += remainder;
    }
	```

# 2023-8
Consider the following excerpt from a program that is supposed to compute the sum of elements in a vector. The function ``worker()`` is called in all processes except process 0, the function ``product()`` is called from process 0.
``` cpp
int product(int nrProc, std::vector<int> const&v) {
	int chunkSize = v.size() / nrProc;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	return sumRec(0, nrProc, chunkSize, v.data());
}

void worker(int myId, int nrProc) {
	int chunkSize;
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
	sumRec(myId, nrProc, chunkSize, nullptr);
}

int sumRec(int myId, int nrProc, int chunkSize, int const* v) {
	std::vector<int> vv(chunkSize);
	MPI_Scatter(v, chunkSize, MPI_INT, vv.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD);
	int sum = 0
	for(int value : vv) {
		sum += value;
	}
	int t = 0;
	if(2*myId+1 < nrProc) {
		MPI_Recv(&t, 1, MPI_INT, 2*myId+1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		sum += t;
	}
	if(2*myId+2 < nrProc) {
		MPI_Recv(&t, 1, MPI_INT, 2*myId+2, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		sum += t;
	}
	if(myId != 0) {
		MPI_Send(&sum, 1, MPI_INT, (myId-1)/2, 1, MPI_COMM_WORLD);
	}
	return sum;
}
```

Which of the following issues are present if the input size plus one is not a multiple of the number of MPI processes? Describe the changes needed to solve them.
- A: the application can have memory corruption.
- B: the application can deadlock.
- C: some worker processes are not used.
- D: some terms are added twice.
- E: **some terms are not added at all**.
- F: the scalar product is incorrectly computed in some other way.

# Solution
- E: Because the chunkSize is rounded down, there will be some elements left over that are not included in the computation.
	- Solution: have the last process handle the remaining elements.
	``` cpp
	// after MPI_Scatter
    if (myId == nrProc - 1) {
		int remainder = v.size() % nrProc;
        chunkSize += remainder;
    }
	```
	
# MPI Theory
MPI has a number of different "send modes". These represent different choices of buffering (where is the data kept until it is received) and synchronization (when does a send complete). In the following, I use "send buffer" for the user-provided buffer to send.
- **MPI_Send** -> will not return until you can use the send buffer. It may or may not block (it is allowed to buffer, either on the sender or receiver side, or to wait for the matching receive).
- **MPI_Ssend** -> will not return until matching receive posted
MPI_Ssend

Blocking communication is done using MPI_Send() and MPI_Recv(). These functions do not return (i.e., they block) until the communication is finished. This means that **MPI_Send() returns when the buffer passed can be reused**, either because MPI saved it somewhere, or because it has been received by the destination. Similarly, **MPI_Recv() returns when the receive buffer has been filled with valid data**.

In contrast, non-blocking communication is done using MPI_Isend() and MPI_Irecv(). These function return immediately (i.e., they do not block) even if the communication is not finished yet. You must call MPI_Wait() or MPI_Test() to see whether the communication has finished.

MPI_Bcast() sends the same piece of data to everyone, while MPI_Scatter() sends each process a part of the input array. 
MPI_Gather() gathers together values from a group of processes.

Scatter -> https://www.mpich.org/static/docs/v3.1/www3/MPI_Scatter.html
```
MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);
int MPI_Bcast( void *buffer, int count, MPI_Datatype datatype, int root, 
               MPI_Comm comm )

MPI_Gather(r, 1, MPI_INT, &sum, 1, MPI_INT, 0, MPI_COMM_WORLD);
int MPI_Gather(const void *sendbuf, int sendcount, MPI_Datatype sendtype,
               void *recvbuf, int recvcount, MPI_Datatype recvtype,
               int root, MPI_Comm comm)
			   
MPI_Scatter(p, chunkSize, MPI_INT, pp.data(), chunkSize, MPI_INT, 0, MPI_COMM_WORLD); // sends p, receives data in pp
int MPI_Scatter(const void *sendbuf, int sendcount, MPI_Datatype sendtype,
               void *recvbuf, int recvcount, MPI_Datatype recvtype, int root,
               MPI_Comm comm)

int MPI_Scatterv(const void *sendbuf, const int *sendcounts, const int *displs,
                 MPI_Datatype sendtype, void *recvbuf, int recvcount,
                 MPI_Datatype recvtype,
                 int root, MPI_Comm comm)

```

![image](https://user-images.githubusercontent.com/53339016/220750330-2578299d-2905-48f2-9e73-5b467334e24a.png)
![image](https://user-images.githubusercontent.com/53339016/220751335-c9cb5bc8-1add-4bc3-bd10-c067c2439208.png)

