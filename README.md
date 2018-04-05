#Orders
Test task to process orders to simulate real-time selling and buying deals. Requires Git, Java8 and Maven installed. 
run.bat files is configured for windows

#How to run
1. clone the repository
2. mvn clean package
3. run.bat for windows
4. Modify input.txt to test with other data
5. Check output.txt for output data

Or alternatively you can use your favourite IDE to run Application.java with a path to input.txt as the only args parameter

#My assumptions:
 - If an order is partially satisfied (by half of its size for instance) its id doesn't change
 - If that order is then cancelled only unsatisfied part of this order will be removed. The deal cannot be undone
 - Prices are greater than 0
 - The highest buying order deals with the lowest selling order although it could have been more profitable to match with the closest price, as they may hugely overlap (and with other prices)
 - Time is more important than memory here. 
 Therefore 4GB of used memory :) 
 This choice might need to be explained in more details. 
 Although we know that we'll have only 10^4 different prices we might have  up to 10^6 - 10^4 + 1 same prices even within 10^4 range therefore counting their total size might appear to be rather costly operation. 
 However if we know that this corner case is irrelevant and prices will be evenly distributed we can apply about 10^4 or even 10^5 pop from a heap operations which could be fine for a real-time system (10^5 * log N)

#Not enough fun?
This file seems to be too small so I'll just leave here the list of my favourite Evans Blue songs
1. Beyond the stars
2. Show me
3. Can't go on
4. The future in the end
5. Halo
6. This time it's different
7. Destroy the obvious
8. Beg
9. Say it
10. Erase my scars 