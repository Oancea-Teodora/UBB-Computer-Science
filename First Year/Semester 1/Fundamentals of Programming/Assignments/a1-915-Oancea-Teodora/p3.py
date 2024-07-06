# Solve the proble8m from the third set here
#Problem 12
def find(d,m,y,cdd,cmm,cyy):
    days=0
    #I create lists with the number of days for each month
    l=[31,28,31,30,31,30,31,31,30,31,30,31]
    lb=[31,29,31,30,31,30,31,31,30,31,30,31]
    idx=0
    #I count the days from the moment of birth to the end of the same year
    if(y%400==0 or (y%4==0 and y%100!=0)):
        if y<cyy:
            days=days+lb[m-1]-d
            for i in range(m,12):
                days=days+lb[i]
    else:
        if y<cyy:
            days=days+l[m-1]-d
            for i in range(m,12):
                days=days+l[i]
    #I count the days for each complete year
    for i in range(y+1,cyy):
        if(i%400==0 or (i%4==0 and i%100!=0)):
            days=days+366
        else:
            days=days+365
    #I count the days from the current year (from 1 Januray to the present)
    if(cyy%400==0 or (cyy%4==0 and cyy%100!=0)):
        if y<cyy:
           while idx<cmm-1:
               days=days+lb[idx]
               idx=idx+1
           days=days+cdd
    else:
        if y<cyy:
            while idx<cmm-1:
               days=days+l[idx]
               idx=idx+1
            #print(days)
            days=days+cdd
    #This is a particular case for people born in the current year
    if y==cyy:
        if m!=cmm:
            if (y%400==0 or (y%4==0 and y%100!=0)):
                days=days+lb[m-1]-d
                for i in range(m,cmm-1):
                    days=days+lb[i]
            else:
                days=days+l[m-1]-d
                for i in range(m,cmm-1):
                    days=days+l[i]
        if m==cmm:
            days=days+cdd-d
        else:
            days=days+cdd
    return days
    
print("Enter your date of birth: ")
day=input()
day=int(day)
month=input()
month=int(month)
year=input()
year=int(year)


print("Enter today's date: ")
cd=input()
cd=int(cd)
cm=input()
cm=int(cm)
cy=input()
cy=int(cy)

print("The age in number of days is: ")
print(find(day,month,year,cd,cm,cy))
