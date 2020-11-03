# n-puzzle
<i>n-puzzle game in Java</i>

This program allows you to generate and play a sliding tile puzzle of size n * n - 1. The recommended size to play is with n=3, or n=4 for a challenge. (n<2 is a no-go unfortunately. Feel free to try n>4, but it won't look pretty ;)

## How to compile and run
To compile, first clone the repo locally, then navigate to `src` and run `javac puzzle/NPuzzleV5.java puzzle/Tile.java`.
After compiling, you can run in a terminal from the `src` folder with `java puzzle.NPuzzleV5`.

## Documentation
You can view code comments in the `.java` files.

## Interesting theory
The crux of this program is the method **chkInversions()**, which ensures that every puzzle generated is actually solvable. This is based off the "inversions" method for determining solvability of a given n-puzzle. If we assume the tiles are displayed in a 1-D array rather than 2-D, a pair of tiles [A, B] form an inversion where A appears before B, but A > B. Take the following example (x is the empty square):
### 2-D puzzle
|  7  |  4  |  6  |

|  x  |  8  |  2  |

|  1  |  3  |  5  |

### 1-D notation
[7, 4, 6, 8, 2, 1, 3, 5]

*Inversion count: 18*


According to the formula for inversion-based solvability:
* if matrix width *n* is odd, the puzzle is solvable only if there are an even number of inversions;
* if *n* is even and the blank square is on an even row counting from the bottom (e.g second-last or fourth-last row), the puzzle is solvable if the number of inversions is odd;
* if *n* is even and the blank square is on an odd row from the bottom (e.g. third-last), the puzzle is solvable if the number of inversions is even.

Thus the given example is solvable by the first set of criteria above.

See **chkInversions()** method comments for details on how I implemented this algorithm. 
