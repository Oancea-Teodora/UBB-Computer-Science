#pragma once
#include <vector>

class Observer
{
public:
	virtual void update() = 0;
	virtual ~Observer() = default;
};

class Subject
{
private:
	std::vector<Observer*> observers;

public:
	void addObserver(Observer* obs);
	void notify();
};

