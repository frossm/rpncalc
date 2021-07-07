import os
import sys
import re
import hashlib

BLOCK_SIZE = 65536 # The size of each read from the file

MD5FILENAME = "CHECKSUM.MD5"
SHA1FILENAME = "CHECKSUM.SHA1"
SHA256FILENAME = "CHECKSUM.SHA256"

if __name__ == "__main__":
    # Detemine the jar filename from our path.  Must be run in project base dir
    fileNamePlain = os.path.basename(os.getcwd()) + ".jar"
    fileName = "target/" + fileNamePlain

    # Create the hash objects
    hashMD5 = hashlib.md5()
    hashSHA1 = hashlib.sha1()
    hashSHA256 = hashlib.sha256()

    # Open the file to read it's contents
    with open(fileName, 'rb') as f:
        # Read the specified amount from the file
        fileBuffer = f.read(BLOCK_SIZE)

        # While there is still data being read from the file...
        while len(fileBuffer) > 0:
            # Update the hashs
            hashMD5.update(fileBuffer)
            hashSHA1.update(fileBuffer)
            hashSHA256.update(fileBuffer)

            # Read the next block from the file
            fileBuffer = f.read(BLOCK_SIZE) 

        # Print the hexadecimal digest of the hash to the console
        print ("Generating Checksums in target directory")
        print (MD5FILENAME + ": " + hashMD5.hexdigest() + "  " + fileNamePlain)
        print (SHA1FILENAME + ": " + hashSHA1.hexdigest() + "  " + fileNamePlain)
        print (SHA256FILENAME + ": " + hashSHA256.hexdigest() + "  " + fileNamePlain)

        # Output the hash information into files
        outFile = open("target/" + MD5FILENAME, "w")
        try:
	        print (hashMD5.hexdigest() + "  " + fileNamePlain, file=outFile)
        finally:
            outFile.close()

        outFile = open("target/" + SHA1FILENAME, "w")
        try:
	        print (hashSHA1.hexdigest() + "  " + fileNamePlain, file=outFile)

        finally:
            outFile.close()

        outFile = open("target/" + SHA256FILENAME, "w")
        try:
	        print (hashSHA256.hexdigest() + "  " + fileNamePlain, file=outFile)

        finally:
            outFile.close()
