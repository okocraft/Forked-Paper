CURRENT_HASH=`cat paper-ref`

git clone https://github.com/PaperMC/Folia check-upstream-work -b master --single-branch
cd check-upstream-work

LATEST_COMMIT_HASH=`git show --format='%H' --no-patch`

if [ $CURRENT_HASH = $LATEST_COMMIT_HASH ]; then
  echo "Everything up-to-date"
  exit 0
fi

echo -n "$LATEST_COMMIT_HASH" > ../paper-ref

COMMITS=`git log --reverse --pretty=format:"%h %s" $CURRENT_HASH..$LATEST_COMMIT_HASH | sed -e 's/\(#[0-9]*\)/Folia\1/g' | sed -e 's/\[ci skip\] //'`

echo "Folia Changes:"
echo "$COMMITS"
echo ""
echo "Latest commit hash: $LATEST_COMMIT_HASH"
echo "See full diff at https://github.com/PaperMC/Folia/compare/$CURRENT_HASH...$LATEST_COMMIT_HASH"

cd ../
rm -rf check-upstream-work

exit 0
