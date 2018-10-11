I did not primarily use the movies.txt file for testing, however, the range of
time it took to load and build the trie was roughly 10 to 16 seconds.

Pokemon.txt was my primary testing file. It took roughly 90,000,000 nanoseconds 
(.09 seconds) to load and build the trie. From the loading.png plot it can be seen that loading and 
building the trie can be done in better than linear time. The loading time is 
monotonically increasing, but with declining magnitude, possibly some sort
of logarithmic time. It should also be noted that time is heteroskadastic by the 
number of words returned (variability increases with the number of words returned)

The slowest part of the code is loading and building the trie. The next 
most time consuming task is autocompleting the K best words, which is less than 
.05% (approximately 50000/90000000 * 100%) of the time, based on the loading time
for 729 words of pokemon.txt and the autocomplete times. 

