# Solve the problem from the second set here
# Problem 6
def find(y,d):
    
    #I create lists with the number of days for each month
    l=[31,28,31,30,31,30,31,31,30,31,30,31]
    lb=[31,29,31,30,31,30,31,31,30,31,30,31]
    nr=1
    idx=0
    #I count the days until I find the month for the day which was introduced
    if y%400==0 or (y%4==0 and y%100!=0):
        while nr+lb[idx]<=d:
            nr=nr+lb[idx]
            idx=idx+1
    else:
        while nr+l[idx]<=d:
            nr=nr+l[idx]
            idx=idx+1
    #I print the month
    print("The date of this day of the year is: ")
    if idx==0:
         print("January ", end="")
    elif idx==1:
        print("February ", end="")
    elif idx==2:
        print("March ", end="")
    elif idx==3:
        print("April ", end="")
    elif idx==4:
        print("Mai ", end="")
    elif idx==5:
        print("June ", end="")
    elif idx==6:
        print("July ", end="")
    elif idx==7:
        print("August ", end="")
    elif idx==8:
        print("Sepember ", end="")
    elif idx==9:
        print("October ", end="")
    elif idx==10:
        print("November ", end="")
    else: 
        print("December ", end="")

    #I find the specific day from that month
    if d-nr+1==1:
        print(d-nr+1, "st",sep="")
    elif d-nr+1==2:
        print(d-nr+1, "nd",sep="")
    elif d-nr+1==3:
        print(d-nr+1,"rd",sep="")
    elif d-nr+1==21:
        print(d-nr+1,"st",sep="")
    elif d-nr+1==22:
        print(d-nr+1,"nd",sep="")
    elif d-nr+1==23:
        print(d-nr+1,"rd",sep="")
    elif d-nr+1==31:
        print(d-nr+1,"st",sep="")
    else:
        print(d-nr+1, "th",sep="")
        
            
print("Enter the year and the day: ")
year=input()
year=int(year)
day=input()
day=int(day)
find(year,day)
