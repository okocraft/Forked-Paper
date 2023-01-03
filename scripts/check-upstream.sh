CURRENT_HASH=`cat paper-ref`

git clone https://github.com/PaperMC/Paper check-upstream-work -b master --single-branch
cd check-upstream-work

LATEST_COMMIT_HASH=`git show --format='%H' --no-patch`

if [ $CURRENT_HASH = $LATEST_COMMIT_HASH ]; then
  echo "Everything up-to-date"
  exit 0
fi

COMMITS=`git log --reverse --pretty=format:"%h %s" $CURRENT_HASH..$LATEST_COMMIT_HASH | sed -e 's/\(#[0-9]*\)/Paper\1/g' | sed -e 's/\[ci skip\] //'`

echo "Paper Changes:"
echo "$COMMITS"
echo ""
echo "Latest commit hash: $LATEST_COMMIT_HASH"
echo "See full diff at https://github.com/PaperMC/Paper/compare/$CURRENT_HASH...$LATEST_COMMIT_HASH"

cd ../
rm -rf check-upstream-work

exit 0
