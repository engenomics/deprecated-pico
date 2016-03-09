# pico
Pico-style compression. The current standard is Pico 1.0.

##how it works so far
Current VCF files are quite inefficient. For each position, they have a separate line, storing the position _and_ the change in the genome.  
Pico 1.0 does the opposite. Given a VCF file, the Pico Translator takes all of the variations in a genome, and gives each variation a certain ID, starting with 0 and sorted by frequency.  
Next, for each variation, a list of positions is created, which contains each position where this variation occurs.  
The IDs and what they represent are stored in the *.idlist files; the lists of variations for each ID are stored in the *.pico files.  

The current compression rate is estimated to be at [currently being tested].


##future improvements
- Fourier comression